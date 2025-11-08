package com.cargohub.product_service.domain.exception;

import com.cargohub.product_service.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ProductErrorCode implements ErrorCode {

    HUB_ID_REQUIRED(HttpStatus.BAD_REQUEST, "허브 ID가 필요합니다."),
    FIRM_ID_REQUIRED(HttpStatus.BAD_REQUEST, "업체 ID가 필요합니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."),

    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 상품에 접근할 권한이 없습니다."),

    OUT_OF_STOCK(HttpStatus.CONFLICT, "해당 상품의 재고가 부족합니다."),
    INVALID_DECREASE_QUANTITY(HttpStatus.BAD_REQUEST, "차감 수량은 0보다 커야 합니다."),
    INVALID_INCREASE_QUANTITY(HttpStatus.BAD_REQUEST, "증가 수량은 0보다 커야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ProductErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
