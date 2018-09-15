package org.ileler.gateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.isAlreadyRouted;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setAlreadyRouted;

/**
 * Author: kerwin612
 */
@Component
@ConditionalOnProperty(value = "gateway.default.redirect", havingValue = "true")
public class GlobalDefaultRedirectFilter implements GlobalFilter, Ordered {

	private static final Log log = LogFactory.getLog(GlobalDefaultRedirectFilter.class);

	private final DispatcherHandler dispatcherHandler;

	public GlobalDefaultRedirectFilter(DispatcherHandler dispatcherHandler) {
		this.dispatcherHandler = dispatcherHandler;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

		String path = requestUrl.getPath();
		if (isAlreadyRouted(exchange) || !"/".equals(path)) {
			return chain.filter(exchange);
		}
		setAlreadyRouted(exchange);

		//TODO: translate url?

		if (log.isTraceEnabled()) {
			log.trace("Redirecting to URI: "+requestUrl);
		}

        ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		response.getHeaders().setLocation(requestUrl);
        return Mono.empty();
	}

}
