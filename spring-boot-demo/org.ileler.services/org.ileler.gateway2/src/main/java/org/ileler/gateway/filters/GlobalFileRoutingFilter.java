package org.ileler.gateway.filters;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.isAlreadyRouted;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setAlreadyRouted;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Author: kerwin612
 */
@Component
public class GlobalFileRoutingFilter implements GlobalFilter, Ordered {

	private static final Log log = LogFactory.getLog(GlobalFileRoutingFilter.class);

	private final DispatcherHandler dispatcherHandler;

	public GlobalFileRoutingFilter(DispatcherHandler dispatcherHandler) {
		this.dispatcherHandler = dispatcherHandler;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = (Route)exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return chain.filter(exchange);
        } else {
            log.trace("GlobalFileRoutingFilter start");

            URI routeUri = route.getUri();
            String scheme = routeUri.getScheme();
            if (isAlreadyRouted(exchange) || !"file".equals(scheme)) {
                return chain.filter(exchange);
            }
            setAlreadyRouted(exchange);

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String filePath = routeUri.getPath() + File.separator + request.getURI().getPath();

            if (log.isTraceEnabled()) {
                log.trace("Reading from file: "+filePath);
            }

            if ("HEAD".equalsIgnoreCase(request.getMethod().toString())) {
                response.getHeaders().set(HttpHeaders.ACCEPT_RANGES, "none");
            } else {
                File file = new File(filePath);
                if (!file.exists()) {
                    response.setStatusCode(HttpStatus.NOT_FOUND);
                } else {
                    DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
                    try {
                        dataBuffer.write(IOUtils.toByteArray(new FileInputStream(file)));
                    } catch (IOException e) {
                        if (log.isErrorEnabled()) {
                            log.error(e);
                        }
                    }
                    return response.writeAndFlushWith(Flux.just(Flux.just(dataBuffer)));
                }
            }

            return Mono.empty();
        }
	}
}
