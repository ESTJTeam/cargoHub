package gateway.gateway.security.domain;

import lombok.Getter;

@Getter
public class TokenBody {
    private Long userId;
    private String role;

    public TokenBody(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
