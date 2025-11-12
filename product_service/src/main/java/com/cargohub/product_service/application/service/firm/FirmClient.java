package com.cargohub.product_service.application.service.firm;//package com.cargohub.product_service.application.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "firms-service", url = "${clients.firm.url}", path = "/v1/firms")
public interface FirmClient {
    // todo: 업체 단건 조회

    @GetMapping("/{firmId}")
    FirmResponseV1 getFirm(@PathVariable UUID firmId);


}
