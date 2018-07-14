package org.ileler.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年07月14日 上午1:57 PM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@SpringBootApplication
@EnableConfigServer
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
