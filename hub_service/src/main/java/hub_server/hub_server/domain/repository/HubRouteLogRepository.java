package hub_server.hub_server.domain.repository;

import hub_server.hub_server.domain.entity.HubRouteLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * HubRouteLog Repository
 * 허브 간 최단 경로 정보를 관리합니다.
 */
public interface HubRouteLogRepository {

    HubRouteLog save(HubRouteLog routeLog);

    Optional<HubRouteLog> findById(UUID id);

    List<HubRouteLog> findAllActive();

    Optional<HubRouteLog> findByStartAndEndHubWithStops(UUID startHubId, UUID endHubId);

    Optional<HubRouteLog> findByStartAndEndHub(UUID startHubId, UUID endHubId);

    void softDeleteByHubId(UUID hubId, UUID deletedBy);

    void softDeleteAll(UUID deletedBy);

    void softDeleteAll();
}
