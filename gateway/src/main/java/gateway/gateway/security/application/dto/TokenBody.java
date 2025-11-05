package gateway.gateway.security.application.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class TokenBody {
    private UUID userId;
    private String role;

    public TokenBody(UUID userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
