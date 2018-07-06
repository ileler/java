package org.ileler.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年07月02日 上午11:16 AM
 * Author:      u~u
 * Version:     1.0.0.0
 * Description: Initialize
 */
@RestController
public class Test {

    @Value("${gateway.test.arg:invalid_test_arg}")
    String arg;

    @GetMapping("/test")
    Mono<String> test() {
        return Mono.just(arg);
    }

}
