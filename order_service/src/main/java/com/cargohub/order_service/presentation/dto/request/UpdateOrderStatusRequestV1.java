package com.cargohub.order_service.presentation.dto.request;

import com.cargohub.order_service.domain.vo.OrderStatus;

public record UpdateOrderStatusRequestV1(
    OrderStatus status
) {
}
