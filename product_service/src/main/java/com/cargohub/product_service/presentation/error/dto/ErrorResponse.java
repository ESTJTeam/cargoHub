package com.cargohub.product_service.presentation.error.dto;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path
) {
}
