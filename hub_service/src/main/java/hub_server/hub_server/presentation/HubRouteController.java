package hub_server.hub_server.presentation;

import hub_server.hub_server.application.dto.query.HubRouteResponseDto;
import hub_server.hub_server.application.service.HubRouteService;
import hub_server.hub_server.common.success.BaseResponse;
import hub_server.hub_server.common.success.BaseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 허브 경로 Controller
 * 허브 간 최단 경로 정보를 조회합니다.
 */
@Slf4j
@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @GetMapping
    public BaseResponse<HubRouteResponseDto> getRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID endHubId,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        HubRouteResponseDto response = hubRouteService.getRoute(startHubId, endHubId, accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping("/from/{startHubId}")
    public BaseResponse<List<HubRouteResponseDto>> getRoutesFromHub(
            @PathVariable UUID startHubId,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        List<HubRouteResponseDto> response = hubRouteService.getRoutesFromHub(startHubId, accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @GetMapping("/all")
    public BaseResponse<List<HubRouteResponseDto>> getAllRoutes(
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        List<HubRouteResponseDto> response = hubRouteService.getAllRoutes(accessToken);
        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
