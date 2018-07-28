package org.ileler.gateway.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.config.ResourceHandlerRegistration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Date:        2018年07月04日 上午2:45 PM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Configuration
@Import(ErrorWebFluxAutoConfiguration.class)
public class StaticResourceConfig implements WebFluxConfigurer {

    private static final Log logger = LogFactory
            .getLog(StaticResourceConfig.class);

    Map<String, List<String>> patternsMap;
    List<StaticResourcePatternDefinition> staticResource;

    public StaticResourceConfig(GatewayProperties properties) {
        staticResource = properties != null ? properties.getStaticResource() : null;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (staticResource == null) return;
        patternsMap = new Hashtable<>(0);
        staticResource.forEach(resource -> {
            String[] patterns = resource.getPatterns();
            ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(patterns).addResourceLocations(resource.getLocations());

            String defaultPage = resource.getDefaultPage();
            if (!StringUtils.isEmpty(defaultPage)) {
                patternsMap.put(defaultPage, Arrays.asList(patterns));
            }

            String cacheControlStr = resource.getCacheControl();
            if (!StringUtils.isEmpty(cacheControlStr)) {
                CacheControl cacheControl;
                if ("no-cache".equalsIgnoreCase(cacheControlStr)) {
                    cacheControl = CacheControl.noCache();
                } else if ("no-store".equalsIgnoreCase(cacheControlStr)) {
                    cacheControl = CacheControl.noStore();
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error(cacheControlStr + " is invalid. support [no-cache, no-store] only.");
                    }
                    return;
                }
                resourceHandlerRegistration.setCacheControl(cacheControl);
            }
        });
        staticResource = null;
    }

    @Bean
    ErrorWebExceptionHandler webExceptionHandler(ErrorWebExceptionHandler defaultErrorWebExceptionHandler) {
        return new WebExceptionHandler(defaultErrorWebExceptionHandler);
    }

    class WebExceptionHandler implements ErrorWebExceptionHandler, Ordered {

        ErrorWebExceptionHandler defaultErrorWebExceptionHandler;

        WebExceptionHandler(ErrorWebExceptionHandler defaultErrorWebExceptionHandler) {
            this.defaultErrorWebExceptionHandler = defaultErrorWebExceptionHandler;
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        @Override
        public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
            if (throwable instanceof ResponseStatusException && HttpStatus.NOT_FOUND.equals(((ResponseStatusException) throwable).getStatus()) && patternsMap != null) {
                String path = serverWebExchange.getRequest().getURI().getPath();
                Iterator<String> iterator = patternsMap.keySet().iterator();
                while(iterator.hasNext()) {
                    String next = iterator.next();
                    List<String> patterns = patternsMap.get(next);
                    for (String pattern : patterns) {
                        if (new AntPathMatcher().match(pattern, path)) {
                            serverWebExchange.getResponse().setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                            serverWebExchange.getResponse().getHeaders().setLocation(URI.create(path + "/" + next));
                            return Mono.empty();
                        }
                    }
                }
            }
            return defaultErrorWebExceptionHandler.handle(serverWebExchange, throwable);
        }

    }
}