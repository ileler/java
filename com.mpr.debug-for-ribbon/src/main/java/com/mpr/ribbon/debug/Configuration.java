package com.mpr.ribbon.debug;

import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Repository;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年12月02日 上午10:11
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@RefreshScope
@Repository("ConfigurationWithDebugForRibbon")
public class Configuration {

    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Value("${service.exclude.hosts:#{null}}")
    private String exclude;

    @Value("${service.mapping.hosts:#{null}}")
    private String mapping;

    @Value("${service.mapping.force:#{false}}")
    private Boolean force;

    private Boolean debug;

    private String[] debugServices;

    public String getExclude() {
        return exclude;
    }

    public String getMapping() {
        return mapping;
    }

    public Boolean getForce() {
        return force;
    }

    public Boolean getDebug() {
        return debug;
    }

    private Map<String, Object> generateConfig(String[] services) {
        Map<String, Object> config = new Hashtable<>(0);
        if (services != null) {
            for (String service : services) {
                config.put(service + ".ribbon.NFLoadBalancerRuleClassName", DebugRule.class.getName());
            }
        }
        return config;
    }

    private void reload() {
        if (configurableEnvironment == null)    return;
        configurableEnvironment.getPropertySources().remove("for-lb-config");
//        if (debug != null && debug) {
            configurableEnvironment.getPropertySources().addFirst(new MapPropertySource("for-lb-config", generateConfig(debugServices)));
//        }
    }

    @Value("${service.lb.debug:#{false}}")
    public void setDebug(Boolean debug) {
        this.debug = debug;
        this.reload();
    }

    @Value("${service.lb.debug.services:#{null}}")
    public void setDebugServices(String[] services) {
        this.debugServices = services;
        this.reload();
    }

    @Bean
    public HystrixConcurrencyStrategy getHystrixConcurrencyStrategy() {
        return new RequestAttributeHystrixConcurrencyStrategy();
    }

}
