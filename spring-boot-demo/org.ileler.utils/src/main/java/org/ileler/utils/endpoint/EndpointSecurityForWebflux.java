package org.ileler.utils.endpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Author: kerwin612
 */
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class EndpointSecurityForWebflux {

    @Value("${security.endpoint.exclud:}")
    String[] excluds;

    @Value("${security.user.name:ops_manager}")
    String username;

    @Value("${security.user.password:ops_microservice}")
    String password;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().matchers(EndpointRequest.toAnyEndpoint().excluding(excluds)).authenticated().and().csrf().disable().httpBasic().and().authorizeExchange().anyExchange().permitAll();
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        return new MapReactiveUserDetailsService(User.withDefaultPasswordEncoder().username(username).password(password).roles("ENDPOINT_ADMIN").build());
    }

}
