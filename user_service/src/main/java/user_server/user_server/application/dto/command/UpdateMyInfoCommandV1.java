package user_server.user_server.application.dto.command;

import lombok.Builder;
import user_server.user_server.domain.entity.Role;

@Builder
public record UpdateMyInfoCommandV1 (
    String slackId,
    String username,
    String nickname,
    String email,
    Role role
){}
