package cargohub.orchestratorservice.infrastructure.client.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderClientRequestV1(
    UUID supplierId,
    UUID receiverId,
    String requestNote,
    List<OrderProductInfo> products
    ) {
    public record OrderProductInfo(
        UUID id,
        String name,
        Integer quantity,
        BigDecimal price
    ){}
}
