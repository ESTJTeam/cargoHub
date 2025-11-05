package user_server.user_server.application.port;

import java.util.UUID;
import user_server.user_server.domain.entity.Role;

public interface TokenIssuer {
    String issueAccessToken(UUID id, Role role, String username);
    String issueRefreshToken(UUID id, Role role, String username);
    boolean validate(String token);
}
