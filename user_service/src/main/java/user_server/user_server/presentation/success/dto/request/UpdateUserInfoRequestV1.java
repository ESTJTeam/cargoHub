package user_server.user_server.presentation.success.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import user_server.user_server.application.dto.command.UpdateUserInfoCommandV1;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;

public record UpdateUserInfoRequestV1(
    String slackId,

    @Size(min = 4, max = 100)
    @Pattern(regexp = "^[a-z0-9]+$", message = "username은 소문자(a~z)와 숫자(0~9)만 가능합니다.")
    String username,

    @Size(max = 100, message = "nickname은 100자 이하이어야 합니다.")
    String nickname,

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255)
    // 선택적으로, 도메인 수준 검증 강화 (예: Gmail, Naver 등 허용)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    String email,

    Role role,

    SignupStatus signupStatus,

    Integer point

) {
    @AssertTrue(message = "role은 DELIVERY_MANAGER 또는 SUPPLIER_MANAGER만 가능합니다.")
    public boolean isValidRole() {
        return role == Role.DELIVERY_MANAGER || role == Role.SUPPLIER_MANAGER;
    }

    public UpdateUserInfoCommandV1 toUpdateUserInfoCommand() {
        return UpdateUserInfoCommandV1.builder()
            .slackId(slackId)
            .email(email)
            .nickname(nickname)
            .username(username)
            .role(role)
            .point(point)
            .signupStatus(signupStatus)
            .build();
    }
}