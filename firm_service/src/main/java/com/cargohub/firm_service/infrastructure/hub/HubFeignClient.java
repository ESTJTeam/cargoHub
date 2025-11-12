package com.cargohub.firm_service.infrastructure.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "hub-service"
)
public interface HubFeignClient {

    @GetMapping("v1/hubs/{hubId}/manager")
    HubResponse getHub(@PathVariable("hubId") UUID hubId);

    // 허브 API 응답 DTO
    class HubResponse {
        public UUID id;
    }
}
