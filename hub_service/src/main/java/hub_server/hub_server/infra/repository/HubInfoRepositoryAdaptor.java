package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubInfo;
import hub_server.hub_server.domain.repository.HubInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubInfoRepositoryAdaptor implements HubInfoRepository {

    private final JpaHubInfoRepository jpaHubInfoRepository;

    @Override
    public HubInfo save(HubInfo hubInfo) {
        return jpaHubInfoRepository.save(hubInfo);
    }

    @Override
    public Optional<HubInfo> findById(UUID id) {
        return jpaHubInfoRepository.findById(id)
                .filter(hubInfo -> hubInfo.getDeletedAt() == null);
    }

    @Override
    public List<HubInfo> findAllActive() {
        return jpaHubInfoRepository.findAllActive();
    }

    @Override
    public Optional<HubInfo> findByStartAndEndHub(UUID startHubId, UUID endHubId) {
        return jpaHubInfoRepository.findByStartAndEndHub(startHubId, endHubId);
    }

    @Override
    public List<HubInfo> findAllByHubId(UUID hubId) {
        return jpaHubInfoRepository.findAllByHubId(hubId);
    }

    @Override
    public boolean existsConnection(UUID hubId1, UUID hubId2) {
        return jpaHubInfoRepository.existsConnection(hubId1, hubId2);
    }
}
