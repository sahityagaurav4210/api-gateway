package me.sgaurav.api_gateway.config;

import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Component
public class ClientIpForwardFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String remoteIp = extractRemoteIp(exchange);

        ServerHttpRequest mutated = request.mutate()
                .headers(h -> {
                    h.remove("X-Forwarded-For");
                    h.remove("X-Real-IP");
                    h.remove("Forwarded");

                    h.set("X-Forwarded-For", remoteIp);
                    h.set("X-Real-IP", remoteIp);
                    h.set("Forwarded", "for=" + remoteIp);
                })
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private String extractRemoteIp(ServerWebExchange exchange) {
        InetSocketAddress addr = exchange.getRequest().getRemoteAddress();
        if (addr == null) return null;
        InetAddress ia = addr.getAddress();
        return ia == null ? addr.getHostString() : ia.getHostAddress();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
