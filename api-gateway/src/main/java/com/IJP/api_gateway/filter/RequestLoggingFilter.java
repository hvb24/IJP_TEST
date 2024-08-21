package com.IJP.api_gateway.filter;

import com.IJP.api_gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("Request Method: {}, Request URI: {}", request.getMethod(), request.getURI());

        if(request.getURI().toString().contains("/auth-service/api/auth/login")||
                request.getURI().toString().contains("/auth-service/api/auth/register")){
            return chain.filter(exchange);
        }

        if (validator.isSecured.test(exchange.getRequest())) {
            //header contains token or not
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
//                template.getForObject("http://auth-service/validate?token=" + authHeader, String.class);
                jwtUtil.validateToken(authHeader);
                Claims claims = jwtUtil.getClaimsFromToken(authHeader);
                List<String> roles = claims.get("roles", List.class);

                String requestUri = request.getURI().toString();
                String method = request.getMethod().toString();

                // Check if the request is for hr-service
                if (requestUri.contains("/hr-service/") || (requestUri.contains("/Jobs/create")&&method.equals("POST"))
                        || (requestUri.contains("/Jobs/jobsByHrId")&&method.equals("GET"))
                        || (requestUri.contains("/Jobs")&&method.equals("DELETE"))|| (requestUri.contains("/Jobs/get-emp-jobId")&&method.equals("GET"))) {
                    if (roles == null || !roles.contains("hr")) {
                        throw new RuntimeException("Unauthorized access: User does not have 'hr' role.");
                    }
                }
            } catch (Exception e) {
                logger.error("Invalid access: Unauthorized access to the application.", e);
                throw new RuntimeException("Unauthorized access to the application");
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
