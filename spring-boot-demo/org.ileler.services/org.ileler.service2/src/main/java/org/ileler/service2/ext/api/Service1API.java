package org.ileler.service2.ext.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: kerwin612
 */
@FeignClient("org.ileler.service1")
public interface Service1API {

    @GetMapping("c1")
    String c1();

}
