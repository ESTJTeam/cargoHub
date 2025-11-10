package user_server.user_server.application.dto.command;

import java.util.UUID;
import lombok.Builder;
import user_server.user_server.domain.entity.Role;

@Builder
public record SignupCommandV1(
    String slackId,
    String password,
    String username,
    String nickname,
    String email,
    Role role,
    UUID hubId
){}