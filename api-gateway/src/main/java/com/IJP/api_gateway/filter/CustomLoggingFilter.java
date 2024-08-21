package com.IJP.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class  CustomLoggingFilter extends AbstractGatewayFilterFactory<CustomLoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingFilter.class);

    public CustomLoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Pre-filter logic
            System.out.println("Custom Pre Filter executed");
            logger.info("Custom Pre Filter executed");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Post-filter logic
                System.out.println("Custom Post Filter executed");
            }));
        };
    }
    public static class Config {
        // Add any custom configuration properties if needed
    }
}
