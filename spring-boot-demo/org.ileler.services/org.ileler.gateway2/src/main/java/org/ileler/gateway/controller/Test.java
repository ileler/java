package org.ileler.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Date:        2018年07月02日 上午11:16 AM
 * Author:      kerwin612
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

    @Value("${foo:}")
    void value1(String value) {
        System.out.println(value);
    }

    @Value("${gateway.default.redirect:}")
    void value2(String value) {
        System.out.println(value);
    }

}
