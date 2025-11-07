package user_server.user_server.infra.sercurity.dto;

import java.util.UUID;
import lombok.Getter;
import user_server.user_server.domain.entity.Role;

@Getter
public class TokenBody {
    private UUID userId;
    private Role role;
    private String username;
    private String type;

    public TokenBody(UUID userId, Role role, String username, String type) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.type = type;
    }
}
