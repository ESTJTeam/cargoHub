package user_server.user_server.application.port;

import java.util.UUID;
import user_server.user_server.domain.entity.Role;

public interface TokenIssuer {
    String issueAccessToken(UUID id, Role role);
    String issueRefreshToken(UUID id, Role role);
    boolean validate(String token);
}
