package hub_server.hub_server.domain.repository;

import hub_server.hub_server.domain.entity.HubRouteLogStop;

import java.util.Optional;
import java.util.UUID;

/**
 * HubRouteLogStop Repository
 * 경로의 경유지 정보를 관리합니다.
 */
public interface HubRouteLogStopRepository {

    HubRouteLogStop save(HubRouteLogStop stop);

    Optional<HubRouteLogStop> findById(UUID id);

    void softDeleteByHubId(UUID hubId, UUID deletedBy);
}
