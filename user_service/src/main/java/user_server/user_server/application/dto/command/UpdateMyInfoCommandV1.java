package user_server.user_server.application.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
