package com.cargohub.firm_service.presentation.controller;


import com.cargohub.firm_service.application.command.CreateFirmCommandV1;
import com.cargohub.firm_service.application.command.UpdateFirmCommandV1;
import com.cargohub.firm_service.application.service.FirmServiceV1;
import com.cargohub.firm_service.common.success.BaseResponse;
import com.cargohub.firm_service.common.success.BaseStatus;
import com.cargohub.firm_service.domain.entity.FirmAddress;
import com.cargohub.firm_service.presentation.dto.request.CreateFirmRequestV1;
import com.cargohub.firm_service.presentation.dto.request.UpdateFirmRequestV1;
import com.cargohub.firm_service.presentation.dto.response.FirmListResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/firms")
@RequiredArgsConstructor
public class FirmControllerV1 {

    private final FirmServiceV1 firmService;

    @GetMapping
    public BaseResponse<FirmListResponseV1> getFirms(@RequestParam UUID hubId,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {

        FirmListResponseV1 response = firmService.getFirmsByHubId(hubId, page, size);

        return BaseResponse.ok(response, BaseStatus.OK);
    }

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

    @PutMapping("/{firmId}")
    @ResponseStatus(HttpStatus.OK)  // 수정이니까 200 OK
    public BaseResponse<Void> updateFirm(@PathVariable UUID firmId,
                                         @RequestBody UpdateFirmRequestV1 request) {

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

        UpdateFirmCommandV1 command = new UpdateFirmCommandV1(
                firmId,
                request.name(),
                request.type(),
                request.hubId(),
                address
        );

        firmService.updateFirm(command);

        // BaseStatus에 UPDATED 같은 거 있으면 그거 쓰고,
        // 없으면 OK로 써도 됨
        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{firmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Void> deleteFirm(@PathVariable UUID firmId,
                                         @RequestHeader("X-User-Id") UUID userId) {
        // userId는 API Gateway가 JWT에서 파싱해서 넣어준 헤더 값
        firmService.deleteFirm(firmId, userId);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
