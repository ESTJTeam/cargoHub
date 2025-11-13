package cargohub.orchestratorservice.infrastructure.client.delivery.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestV1 {

    private UUID orderId;

    private UUID fromHubId;
    private UUID toHubId;
    private UUID destinationId;

    private String supplierHubManagerSlackId;

    @Builder
    public DeliveryRequestV1(
        UUID orderId,
        UUID fromHubId,
        UUID toHubId,
        UUID destinationId,
        String supplierHubManagerSlackId
    ) {
        this.orderId = orderId;
        this.fromHubId = fromHubId;
        this.toHubId = toHubId;
        this.destinationId = destinationId;
        this.supplierHubManagerSlackId = supplierHubManagerSlackId;
    }
}
