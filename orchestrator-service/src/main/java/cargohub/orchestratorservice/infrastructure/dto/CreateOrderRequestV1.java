package cargohub.orchestratorservice.infrastructure.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestV1 (
    UUID receiverId,
    List<OrderProductRequest> products,
    String requestNote
){
    public record OrderProductRequest(
        UUID productId,
        Integer quantity
    ) {}
}

