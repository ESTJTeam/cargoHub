package gateway.gateway.security.filter;

import gateway.gateway.security.application.dto.TokenBody;
import gateway.gateway.security.application.GatewayJwtTokenProvider;
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

        String path = exchange.getRequest().getPath().toString();

        // 로그인, 회원가입은 JWT 검사 생략
        if (path.startsWith("/user/login") || path.startsWith("/user/signup") || path.startsWith("/user/reissue-token")) {
            return chain.filter(exchange);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);


        //TODO 추가로 넣을 예정
        if (path.startsWith("/master") && !"MASTER".equals(tokenBody.getRole())) {
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
