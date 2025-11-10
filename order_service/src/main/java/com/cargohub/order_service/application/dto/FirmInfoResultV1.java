package com.cargohub.order_service.application.dto;

import java.util.UUID;

public record FirmInfoResultV1(
        UUID id,
        String name,
        String address
) {
}
