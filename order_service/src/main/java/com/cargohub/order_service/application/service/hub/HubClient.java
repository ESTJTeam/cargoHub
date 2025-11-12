package com.cargohub.order_service.application.service.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "${clients.hub.url}", path = "/v1/hubs")
public interface HubClient {
    // todo: Hub 조회 - 필요정보(id, name)
    @GetMapping("/{hubId}")
    HubResponseV1 getHub(@PathVariable UUID hubId);

    @GetMapping("/manager/{hubManagerId}")
    Page<HubResponseV1> getHubByManger(@PathVariable("hubManagerId") UUID hubManagerId, @RequestParam(defaultValue = "100") int size);

    // 담당하는 허브인지 검증
    @GetMapping("/hub/{hubId}/manager")
    HubManagerCheckResponseDto checkHubManager(@PathVariable UUID hubId, @RequestHeader("Authorization") String accessToken);
}
