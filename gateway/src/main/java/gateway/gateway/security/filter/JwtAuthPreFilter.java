package gateway.gateway.security.filter;

import gateway.gateway.security.domain.TokenBody;
import gateway.gateway.security.service.GatewayJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthPreFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider jwtTokenProvider;

    public JwtAuthPreFilter(GatewayJwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 필요 시 토큰에서 사용자 정보 추출
        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);

        String path = exchange.getRequest().getPath().toString();

        //TODO 추가로 넣을 예정
        if (path.startsWith("/admin") && !"ADMIN".equals(tokenBody.getRole())) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }


        return chain.filter(exchange);

    }
    @Override
    public int getOrder() {
        return -1; // PostFilter보다 먼저 실행
    }
}
