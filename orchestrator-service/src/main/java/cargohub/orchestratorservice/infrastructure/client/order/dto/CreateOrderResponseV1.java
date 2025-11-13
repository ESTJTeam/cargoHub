package cargohub.orchestratorservice.infrastructure.client.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResponseV1 (
    UUID id,
    UUID supplierId,
    UUID receiverId,
    List<OrderProductResponse> products,
    String requestNote,
    LocalDateTime createdAt
) {
    public record OrderProductResponse(
        UUID id,
        String name,
        Integer quantity,
        BigDecimal price
    ) {}
}
