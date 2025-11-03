package gateway.gateway.security.service;

import gateway.gateway.security.domain.TokenBody;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
@Slf4j
public class GatewayJwtTokenProvider {

    @Value("${custom.jwt.secrets.appkey}")
    private String appkey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(appkey.getBytes());
    }

    // ** 토큰이 유효한지 아닌지 확인 유효성 검사로 access 토큰이 들어오든 refresh 토큰이 들어오든 상관이 없다 그저 형식, 만료 여부만 검샇 **
    public boolean validate(String token) {
        try {
            //검증용 생성
            JwtParser jwtParser = Jwts.parser()
                //내가 만든 키가 맞는지 테스트를 위해 내 비밀키로 설정함
                .verifyWith(getSecretKey())
                .build();

            // 이 부분에서 서명이 변조 되었는지, 만료 시간을 현재 시간과 비교해 유효한지 판단
            jwtParser.parseSignedClaims(token);

            return true; // 성공하면 유효한 토큰이다

        } catch (ExpiredJwtException e) {
            log.debug("토큰 만료됨: {} - {}", maskToken(token), e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 토큰 형식: {} - {}", maskToken(token), e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("구조가 잘못된 토큰: {} - {}", maskToken(token), e.getMessage());
        } catch (JwtException e) {
            log.error("잘못된 토큰 입력됨: {} - {}", maskToken(token), e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 오류: {} - {}", maskToken(token), e.getMessage(), e);
        }
        return false;
    }

    // JWT "토큰에서" user 정보 꺼내기
    public TokenBody parseJwt(String token) {
        //jwts.parset는 JWT를 해석하고 검증하기 위한 파서(parser)를 생성하는 객체
        Jws<Claims> parserd = Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token);
        String userId = parserd.getPayload().getSubject();
        String role = parserd.getPayload().get("role").toString();
        return new TokenBody(Long.parseLong(userId), role);
    }

    //log에서 마스킹 후 출력용
    public String maskToken(String token) {
        if (token == null || token.length() < 10)
            return "(short token)";
        return token.substring(0, 10) + "...(masked)";
    }
}