package com.cargohub.firm_service.infrastructure.hub;

import com.cargohub.firm_service.domain.port.HubValidatorPort;
import com.cargohub.firm_service.domain.vo.HubId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubApiClient implements HubValidatorPort {

    private final HubFeignClient hubFeignClient;

    @Override
    public boolean validateHub(HubId hubId) {
        // Get v1/hubs/{hubId}/manager
        // 200 OK -> true, 404 -> false

        // 아직 api 생성 안됐으므로 임시로 true 반환
        return true;
    }
}
