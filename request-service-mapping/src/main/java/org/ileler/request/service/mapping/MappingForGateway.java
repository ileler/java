package org.ileler.request.service.mapping;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * Author: kerwin612
 */
@Configuration
@ConditionalOnClass(org.springframework.cloud.gateway.filter.LoadBalancerClientFilter.class)
public class MappingForGateway {

    @Bean
    @ConditionalOnBean(SpringClientFactory.class)
    public org.springframework.cloud.gateway.filter.LoadBalancerClientFilter loadBalancerClientFilter(SpringClientFactory clientFactory) {
        return new LoadBalancerClientFilter(new RibbonLoadBalancerClient(clientFactory) {

            private String loadBalancerKey;

            public ServiceInstance choose(String serviceId) {
                String[] strings = serviceId.split("<<>>");
                loadBalancerKey = strings[1];
                return super.choose(strings[0]);
            }

            protected Server getServer(ILoadBalancer loadBalancer) {
                return loadBalancer == null ? null : loadBalancer.chooseServer(StringUtils.isEmpty(loadBalancerKey) ? "default" : loadBalancerKey);
            }

        });
    }

    class LoadBalancerClientFilter extends org.springframework.cloud.gateway.filter.LoadBalancerClientFilter {

        public LoadBalancerClientFilter(LoadBalancerClient loadBalancer) {
            super(loadBalancer);
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            String clientIP = RuleConfiguration.ClientIPHeaderUtil.getClientIP(exchange.getRequest());
            return super.filter(exchange.mutate().request(exchange.getRequest().mutate()
                    .header(RuleConfiguration.ClientIPHeaderUtil.getClientIPHeaderKey(), clientIP)
                    .build()).build(), chain);
        }

        protected ServiceInstance choose(ServerWebExchange exchange) {
            URI attribute = (URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            return loadBalancer.choose(attribute.getHost() + "<<>>" + RuleConfiguration.ClientIPHeaderUtil.getClientIP(exchange.getRequest()));
        }

    }


}
