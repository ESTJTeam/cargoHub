package hub_server.hub_server.domain.repository;

import hub_server.hub_server.domain.entity.HubInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * HubInfo Repository
 * 직접 연결된 허브 간 정보를 관리합니다.
 */
public interface HubInfoRepository {

    HubInfo save(HubInfo hubInfo);

    Optional<HubInfo> findById(UUID id);

    List<HubInfo> findAllActive();

    Optional<HubInfo> findByStartAndEndHub(UUID startHubId, UUID endHubId);

    List<HubInfo> findAllByHubId(UUID hubId);

    boolean existsConnection(UUID hubId1, UUID hubId2);
}
