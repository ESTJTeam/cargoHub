package com.cargohub.order_service.application.service.user;

import com.cargohub.order_service.domain.vo.UserRole;

import java.util.UUID;

public record UserInfoResponseV1(
    UUID userId,
    UserRole role,
    String username,
    String type
) {

    public static UserInfoResponseV1 anonymous() {

        return new UserInfoResponseV1(
                null,
                null,
                "anonymous",
                "anonymous"
        );
    }
}
