package org.ileler.utils.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.IRule;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年07月22日 上午11:58
 * Author:      Cristina
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class DebugRule extends AbstractLoadBalancerRule {

    @Autowired
    IRule deault;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        return null;
    }

}
