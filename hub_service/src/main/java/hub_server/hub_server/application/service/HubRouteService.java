package hub_server.hub_server.application.service;

import hub_server.hub_server.application.dto.query.HubRouteResponseDto;
import hub_server.hub_server.common.error.BusinessException;
import hub_server.hub_server.common.error.ErrorCode;
import hub_server.hub_server.common.security.JwtTokenProvider;
import hub_server.hub_server.domain.entity.HubRouteLog;
import hub_server.hub_server.domain.repository.HubRepository;
import hub_server.hub_server.domain.repository.HubRouteLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * HubRoute 서비스
 * 허브 간 최단 경로 정보를 조회합니다.
 * 경로 정보는 자주 변하지 않으므로 캐싱을 적용합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubRouteService {

    private final HubRouteLogRepository hubRouteLogRepository;
    private final HubRepository hubRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 특정 출발지와 도착지 간 최단 경로 조회
     * 모든 로그인 사용자 가능
     * 캐싱 적용
     *
     * @param startHubId  출발 허브 ID
     * @param endHubId    도착 허브 ID
     * @param accessToken 액세스 토큰
     * @return 경로 정보
     */
    @Cacheable(value = "hubRoute", key = "#startHubId + '_' + #endHubId")
    public HubRouteResponseDto getRoute(UUID startHubId, UUID endHubId, String accessToken) {
        log.info("Getting route from {} to {}", startHubId, endHubId);

        // JWT 파싱 (권한 검증은 로그인 여부만 확인)
        // jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 허브 존재 확인
        if (!hubRepository.existsById(startHubId)) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }
        if (!hubRepository.existsById(endHubId)) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }

        // 경로 조회
        HubRouteLog routeLog = hubRouteLogRepository.findByStartAndEndHubWithStops(startHubId, endHubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_ROUTE_NOT_FOUND));

        return HubRouteResponseDto.from(routeLog);
    }

    /**
     * 특정 출발지에서 다른 모든 허브로의 경로 조회
     * 모든 로그인 사용자 가능
     *
     * @param startHubId  출발 허브 ID
     * @param accessToken 액세스 토큰
     * @return 경로 정보 리스트
     */
    public List<HubRouteResponseDto> getRoutesFromHub(UUID startHubId, String accessToken) {
        log.info("Getting all routes from hub {}", startHubId);

        // JWT 파싱
        jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 허브 존재 확인
        if (!hubRepository.existsById(startHubId)) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }

        // 모든 경로 조회
        List<HubRouteLog> routeLogs = hubRouteLogRepository.findAllActive().stream()
                .filter(log -> log.getStartHub().getId().equals(startHubId))
                .toList();

        return routeLogs.stream()
                .map(HubRouteResponseDto::from)
                .toList();
    }

    /**
     * 모든 경로 조회
     * 모든 로그인 사용자 가능
     *
     * @param accessToken 액세스 토큰
     * @return 경로 정보 리스트
     */
    public List<HubRouteResponseDto> getAllRoutes(String accessToken) {
        log.info("Getting all routes");

        // JWT 파싱
        jwtTokenProvider.parseAuthorizationHeader(accessToken);

        List<HubRouteLog> routeLogs = hubRouteLogRepository.findAllActive();

        return routeLogs.stream()
                .map(HubRouteResponseDto::from)
                .toList();
    }
}
