package user_server.user_server.presentation.success.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import user_server.user_server.application.dto.command.SignupCommandV1;
import user_server.user_server.domain.entity.Role;

public record SignupRequestV1(

    @NotBlank(message = "slackId는 필수입니다.")
    String slackId,

    @NotBlank(message = "password는 필수입니다.")
    @Size(min = 3, max = 100)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$",
        message = "password는 대소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    String password,

    @NotBlank(message = "username은 필수입니다.")
    @Size(min = 4, max = 100)
    @Pattern(regexp = "^[a-z0-9]+$", message = "username은 소문자(a~z)와 숫자(0~9)만 가능합니다.")
    String username,

    @Size(max = 100, message = "nickname은 100자 이하이어야 합니다.")
    String nickname,

    @NotBlank(message = "email은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255, message = "email은 100자 이하이어야 합니다.")
    // 선택적으로, 도메인 수준 검증 강화 (예: Gmail, Naver 등 허용)
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotNull(message = "DELIVERY_MANAGER, SUPPLIER_MANAGER 중 하나를 선택해야 합니다.")
    Role role,

    @NotNull(message = "hubId는 필수입니다.")
    UUID hubId
) {
    @AssertTrue(message = "role은 DELIVERY_MANAGER 또는 SUPPLIER_MANAGER만 가능합니다.")
    public boolean isValidRole() {
        return role == Role.DELIVERY_MANAGER || role == Role.SUPPLIER_MANAGER;
    }

    public SignupCommandV1 toSignupCommandV1() {
        return SignupCommandV1.builder()
            .slackId(slackId)
            .username(username)
            .email(email)
            .nickname(nickname)
            .password(password)
            .role(role)
            .hubId(hubId)
            .build();
    }

}