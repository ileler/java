package org.ileler.demo2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月02日 上午19:48
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Configuration
//@EnableWebMvc
public class Config extends WebMvcConfigurerAdapter {

    /**
     * <mvc:annotation-driven>
            <mvc:argument-resolvers>
                <bean class="org.ileler.demo2.JsonResolver"></bean>
            </mvc:argument-resolvers>
     * </mvc:annotation-driven>
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JsonResolver());
    }

}
