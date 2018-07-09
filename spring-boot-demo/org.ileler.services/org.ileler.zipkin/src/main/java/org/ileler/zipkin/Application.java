package org.ileler.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * Date:        2018年07月07日 上午9:40 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinServer
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
