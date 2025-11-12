package hub_server.hub_server.presentation;

import hub_server.hub_server.application.dto.query.HubInfoResponseDto;
import hub_server.hub_server.application.service.HubInfoService;
import hub_server.hub_server.common.success.BaseResponse;
import hub_server.hub_server.common.success.BaseStatus;
import hub_server.hub_server.presentation.request.CreateHubInfoRequest;
import hub_server.hub_server.presentation.request.UpdateHubInfoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/hub-infos")
@RequiredArgsConstructor
public class HubInfoController {

    private final HubInfoService hubInfoService;

    @PostMapping
    public BaseResponse<HubInfoResponseDto> createHubInfo(
            @Valid @RequestBody CreateHubInfoRequest request,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        HubInfoResponseDto response = hubInfoService.createHubInfo(request.toCommand(), accessToken);
        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

    @PatchMapping("/{hubInfoId}")
    public BaseResponse<HubInfoResponseDto> updateHubInfo(
            @PathVariable UUID hubInfoId,
            @Valid @RequestBody UpdateHubInfoRequest request,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        try {
            HubInfoResponseDto response = hubInfoService.updateHubInfo(request.toCommand(hubInfoId), accessToken);
            return BaseResponse.ok(response, BaseStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/{hubInfoId}")
    public BaseResponse<Void> deleteHubInfo(
            @PathVariable UUID hubInfoId,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        hubInfoService.deleteHubInfo(hubInfoId, accessToken);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @GetMapping("/{hubInfoId}")
    public BaseResponse<HubInfoResponseDto> getHubInfo(
            @PathVariable UUID hubInfoId,
            @RequestHeader("Authorization") String accessToken
    ) {

        HubInfoResponseDto response = hubInfoService.getHubInfo(hubInfoId, accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping
    public BaseResponse<List<HubInfoResponseDto>> getAllHubInfos(
            @RequestHeader("Authorization") String accessToken
    ) {

        List<HubInfoResponseDto> response = hubInfoService.getAllHubInfos(accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping("/hub/{hubId}")
    public BaseResponse<List<HubInfoResponseDto>> getHubInfosByHubId(
            @PathVariable UUID hubId,
            @RequestHeader("Authorization") String accessToken
    ) {

        List<HubInfoResponseDto> response = hubInfoService.getHubInfosByHubId(hubId, accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
