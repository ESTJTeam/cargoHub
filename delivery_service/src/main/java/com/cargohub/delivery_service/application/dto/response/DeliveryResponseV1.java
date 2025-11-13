package com.cargohub.delivery_service.application.dto.response;

import java.util.UUID;

public record DeliveryResponseV1(

    UUID orderId,
    UUID fromHubId,
    UUID toHubId,
    UUID destinationId,

    String receiverSlackId
) { }
