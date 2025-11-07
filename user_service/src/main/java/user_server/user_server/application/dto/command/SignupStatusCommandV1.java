package user_server.user_server.application.dto.command;

import user_server.user_server.domain.entity.SignupStatus;

public record SignupStatusCommandV1 (
    SignupStatus signupStatus
){}
