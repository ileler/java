package org.ileler.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import org.ileler.gateway.config.GatewayProperties;

/**
 * Date:        2018年06月30日 上午10:38 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
@Import(GatewayProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(p -> p
//                        .path("/get")
//                        .filters(f -> f.addRequestHeader("Hello", "World"))
//                        .uri("/"))
//                .build();
//    }

}
