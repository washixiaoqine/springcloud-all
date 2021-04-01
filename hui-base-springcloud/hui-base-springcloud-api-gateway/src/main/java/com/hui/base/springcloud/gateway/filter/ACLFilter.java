package com.hui.base.springcloud.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 权限校验
 */
//@Component
public class ACLFilter  implements GlobalFilter, Ordered {

    @Value("${secretKey:!@#$%^&*}")
    private String secretKey;

    @Value("${issuser:mark}")
    private String issuser;

    //@Autowired
    //private RedisTemplate redisTemplate;

    private static final String TOKEN_CACHE_PREFIX = "auth-service:";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();

        String token = serverHttpRequest.getHeaders().getFirst("token");

        if (StringUtils.isEmpty(token)) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, "未登陆，请重新登陆");
        }
        return chain.filter(exchange);
    }


    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, String msg) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/text;charset=UTF-8");
        return serverHttpResponse.writeWith(Flux.just(serverHttpResponse.bufferFactory().wrap(msg.getBytes())));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
