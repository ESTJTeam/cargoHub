package com.cargohub.order_service.presentation.dto.response;

import com.cargohub.order_service.application.dto.ReadOrderSummaryResultV1;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadOrderSummaryResponseV1(
        UUID id,
        UUID supplierId,
        UUID receiverId,
        OrderStatusResponseV1 status,
        LocalDateTime createdAt

) {
    public static ReadOrderSummaryResponseV1 from(ReadOrderSummaryResultV1 result) {
        return new ReadOrderSummaryResponseV1(
                result.id(),
                result.supplierId(),
                result.receiverId(),
                OrderStatusResponseV1.from(result.status()),
                result.createdAt()
        );
    }
}
