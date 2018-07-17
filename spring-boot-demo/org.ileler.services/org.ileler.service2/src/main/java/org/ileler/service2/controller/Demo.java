package org.ileler.service2.controller;

import org.ileler.service2.ext.api.Service1API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: kerwin612
 */
@RestController
public class Demo {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    @Autowired
    Service1API service1API;

    @GetMapping("c1")
    Mono<String> c1() {
        LOGGER.info("{}", this);
        return Mono.just("s2c1");
    }

    @GetMapping("c2")
    Mono<String> c2() {
        LOGGER.info("{}", this);
        return Mono.just(service1API.c1());
    }
}
