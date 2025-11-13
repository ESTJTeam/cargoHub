package com.cargohub.delivery_service.infrastructure.client.hub;

import com.cargohub.delivery_service.infrastructure.client.hub.dto.HubResponseV1;
import com.cargohub.delivery_service.infrastructure.client.hub.dto.HubRouteResponseV1;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "hub-service", path = "/v1/hubs")
public interface HubClient {

    @GetMapping("/{hubId}")
    boolean validateHub(@PathVariable UUID hubId);

    @GetMapping("/{hubId}/address")
    HubResponseV1 getHub(@PathVariable UUID hubId);

    @GetMapping("/{startHubId}/routes/{endHubId}")
    HubRouteResponseV1 getRouteBetweenHubs(@PathVariable UUID startHubId, @PathVariable UUID endHubId);

}
