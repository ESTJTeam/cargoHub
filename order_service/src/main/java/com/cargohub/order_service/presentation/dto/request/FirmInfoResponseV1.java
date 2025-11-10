package com.cargohub.order_service.presentation.dto.request;

import java.util.UUID;

public record FirmInfoResponseV1(
        UUID id,
        String name,
        String address
) {
}
