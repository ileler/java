package org.ileler.service1.ext.api;

import org.springframework.stereotype.Component;


/**
 * Author: kerwin612
 */
@Component
public class HystrixClientFallback implements Service2API {

    @Override
    public String c1() {
        return "Fallback-c2";
    }

}
