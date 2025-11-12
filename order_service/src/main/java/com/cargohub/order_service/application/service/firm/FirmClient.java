package com.cargohub.order_service.application.service.firm;//package com.cargohub.product_service.application.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "firms-service", url = "${clients.firm.url}", path = "/v1/firms")
public interface FirmClient {

    // 허브별 업체 목록 조회
    @GetMapping
    FirmListResponseV1 getFirmList(@RequestParam("hubId") UUID hubId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "100") int size);

    // 업체 단건 조회
    @GetMapping("/{firmId}")
    FirmResponseV1 getFirm(@PathVariable UUID firmId);


}
