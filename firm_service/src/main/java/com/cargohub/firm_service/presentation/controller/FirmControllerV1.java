package com.cargohub.firm_service.presentation.controller;


import com.cargohub.firm_service.application.command.CreateFirmCommandV1;
import com.cargohub.firm_service.application.service.FirmServiceV1;
import com.cargohub.firm_service.common.success.BaseResponse;
import com.cargohub.firm_service.common.success.BaseStatus;
import com.cargohub.firm_service.domain.entity.FirmAddress;
import com.cargohub.firm_service.presentation.dto.request.CreateFirmRequestV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/firms")
@RequiredArgsConstructor
public class FirmControllerV1 {

    private final FirmServiceV1 firmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createFirm(@RequestBody CreateFirmRequestV1 request) {

        var addrReq = request.address();
        FirmAddress address = new FirmAddress(
                addrReq.postalCode(),
                addrReq.country(),
                addrReq.region(),
                addrReq.city(),
                addrReq.district(),
                addrReq.roadName(),
                addrReq.buildingName(),
                addrReq.detailAddress(),
                addrReq.latitude(),
                addrReq.longitude()
        );

        CreateFirmCommandV1 command = new CreateFirmCommandV1(
                request.name(),
                request.type(),
                request.hubId(),
                address
        );

        firmService.createFirm(command);

        return BaseResponse.ok(
                BaseStatus.CREATED
        );
    }
}
