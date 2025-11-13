package cargohub.orchestratorservice.infrastructure.client.delivery.dto;

import java.util.UUID;

public record DeliveryResponseV1(

    UUID orderId,

    UUID fromHubId,
    UUID toHubId,
    UUID destinationId,

    String supplierUserSlackId
) {

}
