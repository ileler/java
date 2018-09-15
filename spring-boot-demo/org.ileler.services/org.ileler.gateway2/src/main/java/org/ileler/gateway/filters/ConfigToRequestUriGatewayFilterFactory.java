package org.ileler.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.factory.AbstractChangeRequestUriGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: kerwin612
 */
@Component
@RefreshScope
public class ConfigToRequestUriGatewayFilterFactory extends
        AbstractChangeRequestUriGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {
    private final Logger log = LoggerFactory
            .getLogger(ConfigToRequestUriGatewayFilterFactory.class);

    @Value("#{${routes.mapping}}")
    private Map<String, String> mappingMap;

    public ConfigToRequestUriGatewayFilterFactory() {
        super(NameConfig.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(NAME_KEY);
    }

    @Override
    protected Optional<URI> determineRequestUri(ServerWebExchange exchange,
                                                NameConfig config) {
        String prefix = ((PathPattern.PathMatchInfo) (exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE))).getUriVariables().get(config.getName());
        String requestUrl = mappingMap.get(prefix);
        return Optional.ofNullable(requestUrl).map(url -> {
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                log.info("Request url is invalid : url={}, error={}", requestUrl,
                        e.getMessage());
                return null;
            }
        });
    }
}
