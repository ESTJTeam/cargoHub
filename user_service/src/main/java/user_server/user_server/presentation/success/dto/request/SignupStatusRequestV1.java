package user_server.user_server.presentation.success.dto.request;

import user_server.user_server.domain.entity.SignupStatus;

public record SignupStatusRequestV1 (
    SignupStatus signupStatus) {}
