package user_server.user_server.application.dto.command;

import lombok.Builder;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;

@Builder
public record UpdateUserInfoCommandV1 (
    String slackId,
    String username,
    String nickname,
    String email,
    Role role,
    SignupStatus signupStatus,
    Integer point
){}
