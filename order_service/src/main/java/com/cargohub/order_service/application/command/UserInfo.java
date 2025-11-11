package com.cargohub.order_service.application.command;

import com.cargohub.order_service.domain.vo.UserRole;

import java.util.UUID;

public record UserInfo(
        UUID id,
        UserRole role
) {
}
