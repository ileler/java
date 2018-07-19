package org.ileler.service2.ext.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: kerwin612
 */
@FeignClient(name = "org.ileler.service1", fallbackFactory = Service1APIFallbackFactory.class)
public interface Service1API {

    @GetMapping("c1")
    String c1();

    @GetMapping("c3")
    String c2();

}
