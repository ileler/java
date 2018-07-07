package org.ileler.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Date:        2018年06月30日 上午10:38 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@EnableZuulProxy
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
