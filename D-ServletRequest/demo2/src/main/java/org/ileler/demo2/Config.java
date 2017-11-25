package org.ileler.demo2;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月02日 上午19:48
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Configuration
// @EnableWebMvc
public class Config extends WebMvcConfigurerAdapter {

//    /**
//     * <mvc:annotation-driven>
//            <mvc:argument-resolvers>
//                <bean class="org.ileler.demo2.JsonResolver"></bean>
//            </mvc:argument-resolvers>
//     * </mvc:annotation-driven>
//     * @param argumentResolvers
//     */
//    @Override
//    public void addArgumentResolvers(
//            List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new JsonResolver());
//    }

    @Bean
    public AnnotationMethodHandlerAdapter getHandlerAdapter() {
        AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        annotationMethodHandlerAdapter.setMessageConverters(ArrayUtils.insert(0,
                annotationMethodHandlerAdapter.getMessageConverters(),
                new MappingJackson2HttpMessageConverter(),
                new FormHttpMessageConverter()));
        annotationMethodHandlerAdapter.setCustomArgumentResolver(new JsonResolver());
        return annotationMethodHandlerAdapter;
    }

}
