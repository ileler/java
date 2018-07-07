package org.ileler.service1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date:        2018年07月07日 上午9:40 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@RestController
public class Demo {

    @GetMapping("/c1")
    String c1() {
        return "s1c1";
    }

}
