package gateway.gateway.security.application.dto;

import gateway.gateway.domain.Role;
import java.util.UUID;
import lombok.Getter;

@Getter
public class TokenBody {
    private UUID userId;
    private String username;
    private String type;
    private Role role;

    public TokenBody(UUID userId, String username, String type, Role role) {
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.role = role;
    }
}
