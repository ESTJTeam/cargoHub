package com.cargohub.product_service.application.service.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "${clients.hub.url}", path = "/v1/hubs")
public interface HubClient {

    @GetMapping("/{hubId}")
    boolean validateHub(@PathVariable UUID hubId);

    // 담당하는 허브 조회
    @GetMapping("/manager/{hubManagerId}")
    Page<HubResponseV1> getHubsByManager(@PathVariable UUID hubManagerId, @RequestParam(defaultValue = "100") int size);

    @GetMapping("/{hubId}/manager")
    HubManagerCheckResponseV1 checkHubManager(@PathVariable UUID hubId, @RequestHeader("Authorization") String accessToken);
}
