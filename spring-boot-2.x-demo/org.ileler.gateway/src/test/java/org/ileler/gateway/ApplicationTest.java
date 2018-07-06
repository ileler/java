package org.ileler.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年06月27日 上午10:39 AM
 * Author:      u~u
 * Version:     1.0.0.0
 * Description: Initialize
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"gateway.test.arg=test"})
public class ApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void test() throws Exception {
        webClient
                .get().uri("/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        response -> assertThat(new String(response.getResponseBody())).isEqualTo("test"));
    }

}
