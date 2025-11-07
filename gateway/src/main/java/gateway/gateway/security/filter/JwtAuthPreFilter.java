package gateway.gateway.security.filter;

import gateway.gateway.domain.Role;
import gateway.gateway.security.application.GatewayJwtTokenProvider;
import gateway.gateway.security.application.dto.TokenBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthPreFilter implements GlobalFilter, Ordered {

    private final GatewayJwtTokenProvider jwtTokenProvider;

    public JwtAuthPreFilter(GatewayJwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // 로그인, 회원가입 등 화이트리스트 경로는 통과
        if (path.startsWith("/v1/user/login") || path.startsWith("/v1/user/signup") || path.startsWith("/v1/user/reissue-token")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Authorization 추출
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Bearer 제거 + 공백 정리
        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) {
            token = token.substring(6).trim();
        }
        token = token.strip();
        long dots = token.chars().filter(c -> c == '.').count();
        if (dots != 2) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 서명/만료 검증
        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // accessToken만 들어오게 하고 refreshToken은 막음
        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
        if (!tokenBody.getType().equals("access")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (path.startsWith("/v1/master")){
            if (!tokenBody.getRole().equals(Role.MASTER)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }


        // 정규화한 Authorization으로 교체해서 다운스트림으로 전달 + 신뢰 헤더 붙이기도 가능
        final String cleanToken = token;
        ServerHttpRequest mutated = exchange.getRequest().mutate()
            .headers(h -> {
                h.set(HttpHeaders.AUTHORIZATION, cleanToken);
                // h.set("X-User-Id", tokenBody.getUserId().toString()); // 예시
            })
            .build();
        return chain.filter(exchange.mutate().request(mutated).build());

    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
