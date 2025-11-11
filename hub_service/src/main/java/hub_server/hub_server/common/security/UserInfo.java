package hub_server.hub_server.common.security;

import java.util.UUID;

/**
 * JWT 토큰에서 추출한 사용자 정보를 담는 레코드
 */
public record UserInfo(
        UUID userId,
        String username,
        String role,
        String tokenType
) {
}
