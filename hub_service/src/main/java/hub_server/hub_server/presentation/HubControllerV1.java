package hub_server.hub_server.presentation;

import hub_server.hub_server.application.dto.query.HubManagerCheckResponseDto;
import hub_server.hub_server.application.dto.query.HubResponseDto;
import hub_server.hub_server.application.dto.query.HubSimpleResponseDto;
import hub_server.hub_server.application.service.HubService;
import hub_server.hub_server.common.success.BaseResponse;
import hub_server.hub_server.common.success.BaseStatus;
import hub_server.hub_server.presentation.request.CreateHubRequest;
import hub_server.hub_server.presentation.request.HubSearchRequest;
import hub_server.hub_server.presentation.request.UpdateHubRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubControllerV1 {

    private final HubService hubService;

    @PostMapping
    public BaseResponse<HubResponseDto> createHub(
            @Valid @RequestBody CreateHubRequest request,
            @RequestHeader("Authorization") String accessToken
    ) {

        HubResponseDto response = hubService.createHub(request.toCommand(), accessToken);
        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

    @GetMapping("/{hubId}/addresses")
    public BaseResponse<HubResponseDto> getHub(
            @PathVariable UUID hubId
    ) {

        HubResponseDto response = hubService.getHub(hubId);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping("/addresses")
    public BaseResponse<Page<HubSimpleResponseDto>> searchHubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        HubSearchRequest searchRequest = new HubSearchRequest(name, region, city, sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size);

        Page<HubSimpleResponseDto> response = hubService.searchHubs(searchRequest.toCondition(), pageable);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @PatchMapping("/{hubId}")
    public BaseResponse<HubResponseDto> updateHub(
            @PathVariable UUID hubId,
            @Valid @RequestBody UpdateHubRequest request,
            @RequestHeader("Authorization") String accessToken
    ) {

        HubResponseDto response = hubService.updateHub(request.toCommand(hubId), accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @DeleteMapping("/{hubId}")
    public BaseResponse<Void> deleteHub(
            @PathVariable UUID hubId,
            @RequestHeader("Authorization") String accessToken
    ) {

        hubService.deleteHub(hubId, accessToken);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @GetMapping("/{hubId}/manager")
    public BaseResponse<HubManagerCheckResponseDto> checkHubManager(
            @PathVariable UUID hubId,
            @RequestHeader("Authorization") String accessToken
    ) {

        HubManagerCheckResponseDto response = hubService.checkHubManager(hubId, accessToken);

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping("/{hubId}")
    public boolean validateHub(@PathVariable UUID hubId) {

        return hubService.validateHub(hubId);
    }

    @GetMapping("/manager/{hubManagerId}")
    public BaseResponse<Page<HubResponseDto>> getHubsByManagerId(
            @PathVariable UUID hubManagerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String accessToken
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HubResponseDto> response = hubService.getHubsByManagerId(hubManagerId, pageable, accessToken);

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
