package org.ileler.service1.controller;

import org.ileler.service1.ext.api.Service2API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: kerwin612
 */
@RestController
public class Demo {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    @Autowired
    Service2API service2API;

    @GetMapping("/c1")
    String c1() {
        LOGGER.info("{}", this);
        return "s1c1";
    }

    @GetMapping("/c2")
    String c2() {
        LOGGER.info("{}", this);
        return service2API.c1();
    }

}
