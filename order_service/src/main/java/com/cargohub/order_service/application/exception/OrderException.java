package com.cargohub.order_service.application.exception;

import com.cargohub.order_service.common.error.BusinessException;

public class OrderException extends BusinessException {
    public OrderException(OrderErrorCode orderErrorCode) {
        super(orderErrorCode);
    }
}
