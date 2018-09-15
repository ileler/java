package org.ileler.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: kerwin612
 */
@Configuration
public class ServiceUtils {

    private String serviceId;
    private Registration registration;
    private DiscoveryClient discoveryClient;

    public static final ServiceUtils INSTANCE = new ServiceUtils();

    @Bean
    ServiceUtils getInstance(
            @Value("${spring.application.name}") String serviceId,
            Registration registration,
            DiscoveryClient discoveryClient
    ) {
        INSTANCE.serviceId = serviceId;
        INSTANCE.registration = registration;
        INSTANCE.discoveryClient = discoveryClient;
        return INSTANCE;
    }

    public String getServiceInstanceId() {
        return registration.getServiceId() + "@" + registration.getHost() + ":" + registration.getPort();
    }

}
