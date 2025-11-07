package com.cargohub.product_service.common.error;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path
) {
}
