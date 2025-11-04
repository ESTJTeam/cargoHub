package user_server.user_server.application.dto;

import java.util.UUID;
import lombok.Getter;
import user_server.user_server.domain.entity.Role;

@Getter
public class TokenBody {
    private UUID userId;
    private Role role;

    public TokenBody(UUID userId, Role role) {
        this.userId = userId;
        this.role = role;
    }
}
