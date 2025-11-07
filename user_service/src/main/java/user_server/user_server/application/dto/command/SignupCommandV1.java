package user_server.user_server.application.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import user_server.user_server.domain.entity.Role;

@Builder
public record SignupCommandV1(
    String slackId,
    String password,
    String username,
    String nickname,
    String email,
    Role role
){}