package user_server.user_server.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import user_server.user_server.domain.entity.Role;

public record SignupRequest(

    String slackId,

    @NotBlank(message = "password는 필수입니다.")
    @Size(min = 8, max = 15, message = "password는 8~15자여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$",
        message = "password는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    String password,

    @NotBlank(message = "username은 필수입니다.")
    @Size(min = 4, max = 10, message = "username은 4~10자여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "username은 소문자(a~z)와 숫자(0~9)만 가능합니다.")
    String name,

    @NotNull(message = "DELIVERY, FIRM 중 골라주세요.")
    Role role
) {}