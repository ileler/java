package com.mpr.zuul.debug;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年12月02日 上午10:11
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@RefreshScope
@Repository("ConfigurationWithDebugForZuul")
public class Configuration {

    @Value("${service.exclude.hosts:#{null}}")
    private String exclude;

    @Value("${service.mapping.hosts:#{null}}")
    private String mapping;

    @Value("${service.mapping.force:#{false}}")
    private Boolean force;

    @Value("${lb.debug:#{false}}")
    private Boolean debug;

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

}
