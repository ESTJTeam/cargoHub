package com.cargohub.order_service.application.service.user;

import com.cargohub.order_service.domain.vo.UserRole;

import java.util.UUID;

public record UserInfoResponse(
        UUID userId,
        UserRole role,
        String username,
        String type
) {

    public static UserInfoResponse anonymous() {

        return new UserInfoResponse(
                null,
                null,
                "anonymous",
                "anonymous"
        );
    }
}
