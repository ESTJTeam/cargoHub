package user_server.user_server.presentation.success.dto.request;

import user_server.user_server.application.dto.command.SignupStatusCommandV1;
import user_server.user_server.domain.entity.SignupStatus;

public record SignupStatusRequestV1 (
    SignupStatus signupStatus) {

    public SignupStatusCommandV1 toSignupStatusCommand() {
        return new SignupStatusCommandV1(signupStatus);
    }
}
