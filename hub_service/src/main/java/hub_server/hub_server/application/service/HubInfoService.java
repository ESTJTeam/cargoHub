package hub_server.hub_server.application.service;

import hub_server.hub_server.application.dto.command.CreateHubInfoCommand;
import hub_server.hub_server.application.dto.command.UpdateHubInfoCommand;
import hub_server.hub_server.application.dto.query.HubInfoResponseDto;
import hub_server.hub_server.application.dto.vo.ShortestPathResult;
import hub_server.hub_server.common.error.BusinessException;
import hub_server.hub_server.common.error.ErrorCode;
import hub_server.hub_server.common.security.JwtTokenProvider;
import hub_server.hub_server.common.security.UserInfo;
import hub_server.hub_server.domain.entity.Hub;
import hub_server.hub_server.domain.entity.HubInfo;
import hub_server.hub_server.domain.entity.HubRouteLog;
import hub_server.hub_server.domain.repository.HubInfoRepository;
import hub_server.hub_server.domain.repository.HubRepository;
import hub_server.hub_server.domain.repository.HubRouteLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * HubInfo 서비스
 * 허브 간 직접 연결 정보를 관리하고, 변경 시 모든 경로를 재계산합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubInfoService {

    private final HubInfoRepository hubInfoRepository;
    private final HubRepository hubRepository;
    private final HubRouteLogRepository hubRouteLogRepository;
    private final DijkstraRouteCalculator dijkstraCalculator;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 허브 연결 정보 생성
     * MASTER만 가능
     * 생성 후 모든 경로를 재계산합니다.
     */
    @Transactional
    public HubInfoResponseDto createHubInfo(CreateHubInfoCommand command, String accessToken) {
        log.info("Creating HubInfo from {} to {}", command.startHubId(), command.endHubId());

        // JWT 파싱 및 권한 검증 (테스트용 주석)
        // UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);
        // validateMasterRole(userInfo);

        // 허브 존재 여부 확인
        Hub startHub = hubRepository.findById(command.startHubId())
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findById(command.endHubId())
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 중복 확인
        if (hubInfoRepository.existsConnection(command.startHubId(), command.endHubId())) {
            throw new BusinessException(ErrorCode.HUB_INFO_ALREADY_EXISTS);
        }

        // HubInfo 생성
        HubInfo hubInfo = HubInfo.create(
                startHub,
                endHub,
                command.deliveryDuration(),
                command.distance()
        );

        HubInfo savedHubInfo = hubInfoRepository.save(hubInfo);

        // 모든 경로 재계산
        recalculateAllRoutes();

        log.info("HubInfo created with id: {}", savedHubInfo.getId());
        return HubInfoResponseDto.from(savedHubInfo);
    }

    /**
     * 허브 연결 정보 수정
     * MASTER만 가능
     * 수정 후 모든 경로를 재계산합니다.
     */
    @Transactional
    public HubInfoResponseDto updateHubInfo(UpdateHubInfoCommand command, String accessToken) {
        log.info("============ HubInfo 수정 시작 ============");
        log.info("요청 HubInfo ID: {}", command.hubInfoId());
        log.info("수정할 deliveryDuration: {}", command.deliveryDuration());
        log.info("수정할 distance: {}", command.distance());

        // JWT 파싱 및 권한 검증 (테스트용 주석)
        // UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);
        // validateMasterRole(userInfo);

        try {
            log.info("Step 1: HubInfo 조회 중...");
            // HubInfo 조회
            HubInfo hubInfo = hubInfoRepository.findById(command.hubInfoId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));
            log.info("Step 1 완료: HubInfo 찾음 - start: {}, end: {}",
                    hubInfo.getStartHub().getName(), hubInfo.getEndHub().getName());

            log.info("Step 2: HubInfo 업데이트 중...");
            // 수정
            hubInfo.update(command.deliveryDuration(), command.distance());
            log.info("Step 2 완료: HubInfo 업데이트됨");

            log.info("Step 3: 모든 경로 재계산 시작...");
            // 모든 경로 재계산
            recalculateAllRoutes();
            log.info("Step 3 완료: 경로 재계산 완료");

            log.info("============ HubInfo 수정 성공 ============");
            return HubInfoResponseDto.from(hubInfo);
        } catch (Exception e) {
            log.error("❌ HubInfo 수정 중 에러 발생!", e);
            log.error("에러 타입: {}", e.getClass().getName());
            log.error("에러 메시지: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 허브 연결 정보 삭제 (논리 삭제)
     * MASTER만 가능
     * 삭제 후 모든 경로를 재계산합니다.
     */
    @Transactional
    public void deleteHubInfo(UUID hubInfoId, String accessToken) {
        log.info("Deleting HubInfo id: {}", hubInfoId);

        // JWT 파싱 및 권한 검증 (테스트용 주석)
        // UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);
        // validateMasterRole(userInfo);

        // 임시 userId (테스트용)
        UUID tempUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // HubInfo 조회
        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        // 논리 삭제
        hubInfo.delete(tempUserId);

        // 모든 경로 재계산
        recalculateAllRoutes();

        log.info("HubInfo deleted with id: {}", hubInfoId);
    }

    /**
     * 허브 연결 정보 단건 조회
     * 모든 로그인 사용자 가능
     */
    public HubInfoResponseDto getHubInfo(UUID hubInfoId, String accessToken) {
        log.info("Getting HubInfo id: {}", hubInfoId);

        // JWT 파싱 (권한 검증은 로그인 여부만 확인)
        jwtTokenProvider.parseAuthorizationHeader(accessToken);

        HubInfo hubInfo = hubInfoRepository.findById(hubInfoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_INFO_NOT_FOUND));

        return HubInfoResponseDto.from(hubInfo);
    }

    /**
     * 모든 허브 연결 정보 조회
     * 모든 로그인 사용자 가능
     */
    public List<HubInfoResponseDto> getAllHubInfos(String accessToken) {
        log.info("Getting all HubInfos");

        // JWT 파싱
        jwtTokenProvider.parseAuthorizationHeader(accessToken);

        List<HubInfo> hubInfos = hubInfoRepository.findAllActive();

        return hubInfos.stream()
                .map(HubInfoResponseDto::from)
                .toList();
    }

    /**
     * 특정 허브와 연결된 모든 허브 연결 정보 조회
     * 모든 로그인 사용자 가능
     */
    public List<HubInfoResponseDto> getHubInfosByHubId(UUID hubId, String accessToken) {
        log.info("Getting HubInfos for hub id: {}", hubId);

        // JWT 파싱
        jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 허브 존재 확인
        if (!hubRepository.existsById(hubId)) {
            throw new BusinessException(ErrorCode.HUB_NOT_FOUND);
        }

        List<HubInfo> hubInfos = hubInfoRepository.findAllByHubId(hubId);

        return hubInfos.stream()
                .map(HubInfoResponseDto::from)
                .toList();
    }

    /**
     * 모든 경로 재계산
     * HubInfo가 변경될 때마다 호출됩니다.
     * 캐시도 함께 비웁니다.
     */
    @CacheEvict(value = "hubRoute", allEntries = true)
    private void recalculateAllRoutes() {
        log.info("🔄 ========== 경로 재계산 시작 ==========");

        try {
            // 1. 기존 경로 로그 모두 논리 삭제
            log.info("Step 3-1: 기존 경로 삭제 중...");
            hubRouteLogRepository.softDeleteAll();
            log.info("Step 3-1 완료: 기존 경로 삭제됨");

            // 2. 모든 활성 HubInfo 조회
            log.info("Step 3-2: 활성 HubInfo 조회 중...");
            List<HubInfo> activeHubInfos = hubInfoRepository.findAllActive();
            log.info("Step 3-2 완료: 활성 HubInfo {}개 찾음", activeHubInfos.size());

            if (activeHubInfos.isEmpty()) {
                log.warn("⚠️ 활성 HubInfo가 없어 경로 계산 스킵");
                return;
            }

            // 3. 모든 허브 ID 수집
            log.info("Step 3-3: 모든 허브 ID 수집 중...");
            List<UUID> allHubIds = hubRepository.findAll().stream()
                    .filter(hub -> hub.getDeletedAt() == null)
                    .map(Hub::getId)
                    .toList();
            log.info("Step 3-3 완료: 활성 허브 {}개 찾음", allHubIds.size());

            // 4. 다익스트라 알고리즘으로 모든 경로 계산
            log.info("Step 3-4: Dijkstra 알고리즘 실행 중...");
            List<ShortestPathResult> shortestPaths = dijkstraCalculator.calculateAllShortestPaths(
                    allHubIds,
                    activeHubInfos
            );
            log.info("Step 3-4 완료: {}개 경로 계산됨", shortestPaths.size());

            // 5. Hub ID로 Hub 엔티티 조회를 위한 Map 생성
            log.info("Step 3-5: Hub 엔티티 Map 생성 중...");
            Map<UUID, Hub> hubMap = hubRepository.findAllById(allHubIds).stream()
                    .collect(Collectors.toMap(Hub::getId, hub -> hub));
            log.info("Step 3-5 완료: Hub Map 생성됨");

            // 6. 계산 결과를 DB에 저장
            log.info("Step 3-6: 경로 DB 저장 중... ({}개)", shortestPaths.size());
            int savedCount = 0;
            for (ShortestPathResult result : shortestPaths) {
                Hub startHub = hubMap.get(result.startHubId());
                Hub endHub = hubMap.get(result.endHubId());

                if (startHub == null || endHub == null) {
                    log.warn("⚠️ Hub not found for route: {} -> {}", result.startHubId(), result.endHubId());
                    continue;
                }

                // HubRouteLog 생성
                HubRouteLog routeLog = HubRouteLog.create(
                        startHub,
                        endHub,
                        result.totalDuration(),
                        result.totalDistance()
                );

                // 경유지 설정
                List<Hub> stopHubs = result.path().stream()
                        .map(hubMap::get)
                        .filter(hub -> hub != null)
                        .toList();

                routeLog.setStops(stopHubs);

                // 저장
                hubRouteLogRepository.save(routeLog);
                savedCount++;
            }

            log.info("Step 3-6 완료: {}개 경로 저장됨", savedCount);
            log.info("✅ ========== 경로 재계산 완료 ==========");

        } catch (Exception e) {
            log.error("❌ 경로 재계산 중 에러 발생!", e);
            log.error("에러 타입: {}", e.getClass().getName());
            log.error("에러 메시지: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * MASTER 권한 검증
     */
    private void validateMasterRole(UserInfo userInfo) {
        if (!"MASTER".equals(userInfo.role())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
