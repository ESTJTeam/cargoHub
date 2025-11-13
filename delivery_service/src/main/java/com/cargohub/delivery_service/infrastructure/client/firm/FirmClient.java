package com.cargohub.delivery_service.infrastructure.client.firm;

import com.cargohub.delivery_service.infrastructure.client.firm.dto.FirmListResponseV1;
import com.cargohub.delivery_service.infrastructure.client.firm.dto.FirmResponseV1;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "firms-service", path = "/v1/firms")
public interface FirmClient {

    // 허브별 업체 목록 조회
    @GetMapping
    FirmListResponseV1 getFirmList(@RequestParam("hubId") UUID hubId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "100") int size);

    // 업체 단건 조회
    @GetMapping("/{firmId}")
    FirmResponseV1 getFirm(@PathVariable UUID firmId);

}