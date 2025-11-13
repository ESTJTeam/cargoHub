//package cargohub.orchestratorservice.libs.security;
//
//import cargohub.orchestratorservice.application.dto.UserInfoResponse;
//import cargohub.orchestratorservice.domain.UserRole;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//import javax.crypto.SecretKey;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class JwtUtil {
//
//    @Value("${custom.jwt.secrets.appkey}")
//    private String appkey;
//
//
//    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(appkey.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public UserInfoResponse parseJwt(String token) {
//        Jws<Claims> parsed = Jwts.parser()
//                .verifyWith(getSecretKey())
//                .build()
//                .parseSignedClaims(token);
//
//        String userId = parsed.getPayload().getSubject();
//        String role = parsed.getPayload().get("role", String.class);
//        String username = parsed.getPayload().get("username", String.class);
//        String type = parsed.getPayload().get("type", String.class);
//
//        return new UserInfoResponse(UUID.fromString(userId), UserRole.valueOf(role), username, type);
//    }
//}
