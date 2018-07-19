package org.ileler.service2.ext.api;

import org.springframework.stereotype.Component;


/**
 * Author: kerwin612
 */
@Component
public class HystrixClientFallback implements Service1API {

    @Override
    public String c1() {
        return "Fallback-c2";
    }

    @Override
    public String c2() {
        return "Fallback-c3";
    }

}
