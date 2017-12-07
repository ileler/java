package com.mpr.zuul.debug;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月29日 上午11:39
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class DebugRule extends RoundRobinRule implements BeanFactoryAware {

    public static class Enable implements SpringApplicationRunListener {

        @Override
        public void starting() {
        }

        private Map<String, String> resolveSource(Object source) {
            Map<String, String> result = new Hashtable<>(0);
            if (source instanceof PropertySource) {
                result.putAll(resolveSource(((PropertySource) source).getSource()));
            } else if (source instanceof Iterable) {
                Iterator iterator = ((Iterable) source).iterator();
                while (iterator.hasNext()) {
                    result.putAll(resolveSource(iterator.next()));
                }
            } else if (source instanceof Map) {
                result.putAll((Map) source);
            }
            return result;
        }

        private Map<String, Object> generateConfig(Object source) {
            Map<String, String> map = resolveSource(source);
            if (map == null)    return null;
            Map<String, Object> config = new Hashtable<>(0);
            Iterator<String> iterator = map.keySet().iterator();
            Pattern compile = Pattern.compile("zuul\\.routes\\..*\\.service-?[iI]d");
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (compile.matcher(next).matches()) {
                    config.put(map.get(next) + ".ribbon.NFLoadBalancerRuleClassName", DebugRule.class.getName());
                }
            }
            return config;
        }

        @Override
        public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {
            if (configurableEnvironment.getProperty("lb.debug", Boolean.class, false)) configurableEnvironment.getPropertySources().addFirst(new MapPropertySource("for-lb-config", generateConfig(configurableEnvironment.getPropertySources())));
        }

        @Override
        public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
        }

        @Override
        public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
        }

        @Override
        public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
        }

        public Enable(SpringApplication application, String[] args) {
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugRule.class);

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        return choose(getLoadBalancer(), o);
    }

    public Server choose(ILoadBalancer lb, Object key) {

        Boolean debug = null;
        Boolean force = null;
        String mapping = null;
        String exclude = null;

        try {
            Object object = this.beanFactory.getBean("ConfigurationWithDebugForZuul");

            Method method = object.getClass().getMethod("getDebug");
            Object debugObject = method.invoke(object);
            debug = debugObject == null ? null : Boolean.valueOf(debugObject.toString());

            method = object.getClass().getMethod("getMapping");
            Object mappingObject = method.invoke(object);
            mapping = mappingObject == null ? null : mappingObject.toString();

            method = object.getClass().getMethod("getForce");
            Object forceObject = method.invoke(object);
            force = forceObject == null ? null : Boolean.valueOf(forceObject.toString());

            method = object.getClass().getMethod("getExclude");
            Object excludeObject = method.invoke(object);
            exclude = excludeObject == null ? null : excludeObject.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        String info = "";
        if (LOGGER.isInfoEnabled()) {
            info += "\ndebug:[" + (debug == null ? "null" : debug.toString()) + "]\n"
                    + "force:[" + (force == null ? "null" : force.toString()) + "]\n"
                    + "mapping:[" + (mapping == null ? "null" : mapping.toString()) + "]\n"
                    + "exclude:[" + (exclude == null ? "null" : exclude.toString()) + "]\n";
        }
        if (debug == null || !debug)     return super.choose(key);

        if (lb == null) {
            LOGGER.warn("no load balancer");
            return null;
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx == null ? null : ctx.getRequest();

        if (LOGGER.isInfoEnabled() && request != null) {
            info += "request:" + request.getRequestURL().toString() + "\n";
        }

        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = new ArrayList<>(lb.getAllServers());
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                LOGGER.warn("No up servers available from load balancer: " + lb);
                return null;
            }

            String dhost = request == null ? null : request.getParameter("_d_s_h_");
            if (request != null && StringUtils.isEmpty(dhost) && !StringUtils.isEmpty(mapping)) {
                String remoteHost = request.getRemoteHost();
                String[] split = mapping.split(";");
                for (String str : split) {
                    if (str.startsWith(remoteHost + ":")) {
                        dhost = str.split(remoteHost + ":")[1];
                        break;
                    }
                }
            }
            if (!StringUtils.isEmpty(exclude)) {
                Iterator<Server> iterator = allServers.iterator();
                while (iterator.hasNext()) {
                    Server _server = iterator.next();
                    if (!StringUtils.isEmpty(dhost) && dhost.contains(_server.getHost())) continue;
                    if (Pattern.compile(exclude).matcher(_server.getHost()).matches()) {
                        _server.setReadyToServe(false);
                        _server.setAlive(false);
                    }
                }
            }

            if (LOGGER.isInfoEnabled() && !info.contains("dhost:[")) {
                info += "dhost:[" + (dhost == null ? "null" : dhost.toString()) + "]\n";
            }

            if (!StringUtils.isEmpty(dhost)) {
                if (LOGGER.isInfoEnabled()) {
                    info += "servers:[";
                }
                for (int i = 0, j = allServers.size(); i < j; i++) {
                    Server _server = allServers.get(i);
                    if (_server == null) continue;
                    if (LOGGER.isInfoEnabled()) {
                        info += (i == 0 ? "" : ",") + _server.getHost();
                    }
                    if (dhost.contains(_server.getHost())) {
                        _server.setReadyToServe(true);
                        _server.setAlive(true);
                        server = _server;
                        break;
                    }
                }
                if (LOGGER.isInfoEnabled()) {
                    info += "]\n";
                }
            }
            if ((StringUtils.isEmpty(dhost) && server == null) || (!StringUtils.isEmpty(dhost) && !force)) {
                server = super.choose(lb, key);
            }

            if (server != null && server.isAlive() && (server.isReadyToServe())) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(info + "chosen:" + server.getHost());
                }
                return (server);
            }

            // Next.
            server = null;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(info);
        }
        if (count >= 10) {
            LOGGER.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;

    }

}