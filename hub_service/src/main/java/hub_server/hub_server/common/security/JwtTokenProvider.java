package hub_server.hub_server.common.security;

import hub_server.hub_server.common.error.BusinessException;
import hub_server.hub_server.common.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${custom.jwt.secrets.originkey}") String secretKeyString) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     * "Bearer {token}" 형식에서 토큰 부분만 반환
     */
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        }
        return authorizationHeader.substring(7);
    }

    /**
     * JWT 토큰을 파싱하여 UserInfo 객체 반환
     */
    public UserInfo parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 토큰 타입 확인 (Access Token만 허용)
            String tokenType = claims.get("tokenType", String.class);
            if (!"access".equals(tokenType)) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
            }

            UUID userId = UUID.fromString(claims.get("userId", String.class));
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            return new UserInfo(userId, username, role, tokenType);

        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (SignatureException e) {
            log.error("JWT signature validation failed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (IllegalArgumentException e) {
            log.error("JWT token is invalid: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    /**
     * Authorization 헤더를 받아서 토큰 추출 및 파싱을 한 번에 처리
     */
    public UserInfo parseAuthorizationHeader(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        return parseToken(token);
    }
}
