package org.ileler.service1.ext.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: kerwin612
 */
@FeignClient(name = "org.ileler.service2", fallback = HystrixClientFallback.class)
public interface Service2API {

    @GetMapping("c1")
    String c1();

}
