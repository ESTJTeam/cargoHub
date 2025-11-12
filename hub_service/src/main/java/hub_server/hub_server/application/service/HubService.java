package hub_server.hub_server.application.service;

import hub_server.hub_server.application.dto.command.CreateHubCommandV1;
import hub_server.hub_server.application.dto.command.UpdateHubCommandV1;
import hub_server.hub_server.application.dto.query.HubManagerCheckResponseDto;
import hub_server.hub_server.application.dto.query.HubResponseDto;
import hub_server.hub_server.application.dto.query.HubSearchCondition;
import hub_server.hub_server.application.dto.query.HubSimpleResponseDto;
import hub_server.hub_server.application.mapper.HubMapper;
import hub_server.hub_server.common.error.BusinessException;
import hub_server.hub_server.common.error.ErrorCode;
import hub_server.hub_server.common.security.JwtTokenProvider;
import hub_server.hub_server.common.security.UserInfo;
import hub_server.hub_server.domain.entity.Hub;
import hub_server.hub_server.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

    private final HubRepository hubRepository;
    private final HubMapper hubMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public HubResponseDto createHub(CreateHubCommandV1 command, String accessToken) {

        // JWT 토큰 파싱 및 검증 (Access Token만 허용)
        UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 권한 검증: MASTER만 허브 생성 가능
        if (!"MASTER".equals(userInfo.role())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 허브명 중복 검증
        if (hubRepository.existsByName(command.name())) {
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        // 현재 로그인한 사용자를 허브 관리자로 설정
        UUID hubManagerId = userInfo.userId();

        // 허브 생성
        Hub hub = hubMapper.toEntity(command, hubManagerId);
        Hub savedHub = hubRepository.save(hub);

        return hubMapper.toResponseDto(savedHub);
    }

    public HubResponseDto getHub(UUID hubId) {

        Hub hub = hubRepository.findByIdWithAddress(hubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        return hubMapper.toResponseDto(hub);
    }

    /**
     * 허브 검색 (페이징)
     * 권한: 모든 로그인 사용자
     */
    public Page<HubSimpleResponseDto> searchHubs(HubSearchCondition condition, Pageable pageable) {

        // 페이지 크기 검증 (10, 30, 50만 허용)
        validatePageSize(pageable.getPageSize());

        Page<Hub> hubs = hubRepository.search(condition, pageable);
        return hubs.map(hubMapper::toSimpleResponseDto);
    }

    @Transactional
    public HubResponseDto updateHub(UpdateHubCommandV1 command, String accessToken) {

        // JWT 토큰 파싱 및 검증 (Access Token만 허용)
        UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 허브 조회
        Hub hub = hubRepository.findByIdWithAddress(command.hubId())
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // 권한 검증: MASTER 또는 해당 허브의 HUB_MANAGER만 수정 가능
        if (!"MASTER".equals(userInfo.role())) {
            if (!"HUB_MANAGER".equals(userInfo.role()) || !hub.getHubManagerId().equals(userInfo.userId())) {
                throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
            }
        }

        // 허브명 중복 검증
        if (hubRepository.existsByNameAndIdNot(command.name(), command.hubId())) {
            throw new BusinessException(ErrorCode.HUB_ALREADY_EXISTS);
        }

        UUID hubManagerId = hub.getHubManagerId();

        hub.update(command.name(), hubManagerId);

        if (hub.getHubAddress() != null) {
            hub.getHubAddress().update(
                    command.postalCode(),
                    command.country(),
                    command.region(),
                    command.city(),
                    command.district(),
                    command.roadName(),
                    command.buildingName(),
                    command.detailAddress(),
                    command.fullAddress(),
                    command.latitude(),
                    command.longitude()
            );
        }

        Hub updatedHub = hubRepository.save(hub);
        log.info("Hub updated successfully with id: {}", updatedHub.getId());

        return hubMapper.toResponseDto(updatedHub);
    }

    @Transactional
    public void deleteHub(UUID hubId, String accessToken) {

        // JWT 토큰 파싱 및 검증 (Access Token만 허용)
        UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);

        // 권한 검증: MASTER만 허브 삭제 가능
        if (!"MASTER".equals(userInfo.role())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        hub.delete(userInfo.userId());
        hubRepository.save(hub);

        log.info("Hub deleted successfully with id: {}", hubId);
    }

    public HubManagerCheckResponseDto checkHubManager(UUID hubId, String accessToken) {

        // JWT 토큰 파싱 및 검증 (Access Token만 허용)
        UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);

        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));

        // MASTER는 모든 허브에 대해 관리자
        if ("MASTER".equals(userInfo.role())) {
            return new HubManagerCheckResponseDto(true);
        }

        // HUB_MANAGER는 본인이 관리하는 허브만 관리자
        boolean isManager = hub.getHubManagerId().equals(userInfo.userId());

        return new HubManagerCheckResponseDto(isManager);
    }

    public boolean validateHub(UUID hubId) {

        return hubRepository.existsById(hubId);
    }

    /**
     * 권한: 로그인한 사용자 (본인이 관리하는 허브만 조회 가능, MASTER는 모든 매니저의 허브 조회 가능)
     */
    public Page<HubResponseDto> getHubsByManagerId(UUID hubManagerId, Pageable pageable, String accessToken) {

        // // JWT 토큰 파싱 및 검증
        // UserInfo userInfo = jwtTokenProvider.parseAuthorizationHeader(accessToken);
        //
        // if (!"MASTER".equals(userInfo.role()) && !userInfo.userId().equals(hubManagerId)) {
        //     throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        // }

        validatePageSize(pageable.getPageSize());

        Page<Hub> hubs = hubRepository.findByHubManagerIdWithPaging(hubManagerId, pageable);

        return hubs.map(hubMapper::toResponseDto);
    }

    private void validatePageSize(int pageSize) {
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            throw new BusinessException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }
}
