package org.ileler.service2.ext.api;

import feign.hystrix.FallbackFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: kerwin612
 */
@Component
public class Service1APIFallbackFactory implements FallbackFactory<Service1API> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service1APIFallbackFactory.class);

    @Autowired
    HystrixClientFallback hystrixClientFallback;

    @Override
    public Service1API create(Throwable throwable) {
        if (
                !(throwable instanceof RuntimeException && StringUtils.isBlank(throwable.getMessage()))
                ) {
            LOGGER.info("fallback; reason was: {}, {}", throwable.getMessage(), throwable);
        }
        return hystrixClientFallback;
    }
}
