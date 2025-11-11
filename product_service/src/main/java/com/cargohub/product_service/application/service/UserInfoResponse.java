package com.cargohub.product_service.application.service;

import com.cargohub.product_service.domain.vo.UserRole;

import java.util.UUID;

public record UserInfoResponse(
        UUID userId,
        UserRole role,
        String username,
        String type
) {
}
