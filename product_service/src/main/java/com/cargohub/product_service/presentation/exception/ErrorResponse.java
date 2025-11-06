package com.cargohub.product_service.presentation.exception;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path
) {
}
