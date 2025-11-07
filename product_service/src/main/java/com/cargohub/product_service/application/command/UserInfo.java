package com.cargohub.product_service.application.command;

import com.cargohub.product_service.domain.vo.UserRole;

import java.util.UUID;

public record UserInfo(
        UUID id,
        UserRole role
) {
}
