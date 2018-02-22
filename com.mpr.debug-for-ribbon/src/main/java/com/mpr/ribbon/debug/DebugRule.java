package com.mpr.ribbon.debug;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;


/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月29日 上午11:39
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class DebugRule extends RoundRobinRule implements BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugRule.class);

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
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
            Object object = this.beanFactory.getBean("ConfigurationWithDebugForRibbon");

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

        if (debug == null || !debug)     return super.choose(lb, key);

        if (lb == null) {
            LOGGER.warn("no load balancer");
            return null;
        }

        HttpServletRequest request = Util.getCurrentRequest();

        Server server = null;
        String info = "";
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

            String dfrom = request == null ? null : request.getParameter("_d_s_h_");
            if (request != null && StringUtils.isEmpty(dfrom) && !StringUtils.isEmpty(mapping)) {
                String remoteIP = Util.getIP(request);
                String[] split = mapping.split(";");
                for (String str : split) {
                    if (str.startsWith(remoteIP + ":")) {
                        dfrom = str.split(remoteIP + ":")[1];
                        break;
                    }
                }
            }
            if (!StringUtils.isEmpty(exclude)) {
                Iterator<Server> iterator = allServers.iterator();
                while (iterator.hasNext()) {
                    Server _server = iterator.next();
                    if (!StringUtils.isEmpty(dfrom) && dfrom.contains(_server.getHost())) continue;
                    if (Pattern.compile(exclude).matcher(_server.getHost()).matches()) {
                        _server.setReadyToServe(false);
                        _server.setAlive(false);
                    }
                }
            }

            if (LOGGER.isDebugEnabled() && !info.contains("dfrom:[")) {
                info += "request:" + (request != null ? request.getRequestURL().toString() : "unknow") + "\n"
                            + "\tmapping:[" + (mapping == null ? "none" : mapping.toString()) + "]\n"
                            + "\texclude:[" + (exclude == null ? "none" : exclude.toString()) + "]\n"
                            + "\tdebug:[" + (debug == null ? "false" : debug.toString()) + "]\t" + "force:[" + (force == null ? "false" : force.toString()) + "]\t" + "dfrom:[" + (dfrom == null ? "none" : dfrom.toString()) + "]\n";
            }

            if (!StringUtils.isEmpty(dfrom)) {
                if (LOGGER.isDebugEnabled()) {
                    info += "\tservers:[";
                }
                for (int i = 0, j = allServers.size(); i < j; i++) {
                    Server _server = allServers.get(i);
                    if (_server == null) continue;
                    if (LOGGER.isDebugEnabled()) {
                        info += (i == 0 ? "" : ",") + _server.getHost();
                    }
                    if (dfrom.contains(_server.getHost()) && server == null) {
                        _server.setReadyToServe(true);
                        _server.setAlive(true);
                        server = _server;
//                        break;
                    }
                }
                if (LOGGER.isDebugEnabled()) {
                    info += "]\n";
                }
            }
            if ((StringUtils.isEmpty(dfrom) || (!StringUtils.isEmpty(dfrom) && !force)) && server == null) {
                server = super.choose(lb, key);
            }

            if (server != null && server.isAlive() && (server.isReadyToServe())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(info + "\tchosen:" + server.getHost());
                }
                return (server);
            }

            // Next.
            server = null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(info);
        }
        if (count >= 10) {
            LOGGER.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;

    }

}