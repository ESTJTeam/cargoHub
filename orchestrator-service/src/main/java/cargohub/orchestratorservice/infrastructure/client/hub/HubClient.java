package cargohub.orchestratorservice.infrastructure.client.hub;

import cargohub.orchestratorservice.infrastructure.client.hub.dto.HubManagerCheckResponseDto;
import cargohub.orchestratorservice.infrastructure.client.hub.dto.HubResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", path = "/v1/hubs")
public interface HubClient {

    @GetMapping("/{hubId}")
    boolean validateHub(@PathVariable UUID hubId);

    @GetMapping("/{hubId}/address")
    HubResponseV1 getHub(@PathVariable UUID hubId);

    @GetMapping("/manager/{hubManagerId}")
    Page<HubResponseV1> getHubByManger(@PathVariable("hubManagerId") UUID hubManagerId, @RequestParam(defaultValue = "100") int size);

    // 담당하는 허브인지 검증
    @GetMapping("/{hubId}/manager")
    HubManagerCheckResponseDto checkHubManager(@PathVariable UUID hubId, @RequestHeader("Authorization") String accessToken);

}
