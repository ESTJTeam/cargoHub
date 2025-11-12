package com.cargohub.product_service.application.service.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "${clients.hub.url}", path = "/v1/hubs")
public interface HubClient {
    // todo: Hub 조회 - 필요정보(id, name)
    @GetMapping("/{hubId}")
    HubResponseV1 getHub(@PathVariable UUID hubId);


    // 담당하는 허브 조회
    @GetMapping("/{hubId}/manager")
    HubManagerCheckResponseV1 checkHubManager(@PathVariable UUID hubId, @RequestHeader("Authorization") String accessToken);
}
