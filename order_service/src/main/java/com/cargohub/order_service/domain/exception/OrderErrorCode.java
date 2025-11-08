package com.cargohub.order_service.domain.exception;

import com.cargohub.order_service.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {

    INVALID_ORDER_SUPPLIER(HttpStatus.BAD_REQUEST, "같은 공급 업체 상품만 주문할 수 있습니다."),
    ORDER_PRODUCT_EMPTY(HttpStatus.BAD_REQUEST, "주문 항목은 최소 1개 이상이어야 합니다."),
    ORDER_CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "현재 상태에서는 주문을 취소할 수 없습니다."),

    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 주문에 접근할 권한이 없습니다."),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    OrderErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
