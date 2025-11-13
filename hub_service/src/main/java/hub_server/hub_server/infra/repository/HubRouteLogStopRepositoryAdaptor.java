package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubRouteLogStop;
import hub_server.hub_server.domain.repository.HubRouteLogStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteLogStopRepositoryAdaptor implements HubRouteLogStopRepository {

    private final JpaHubRouteLogStopRepository jpaHubRouteLogStopRepository;

    @Override
    public HubRouteLogStop save(HubRouteLogStop stop) {
        return jpaHubRouteLogStopRepository.save(stop);
    }

    @Override
    public Optional<HubRouteLogStop> findById(UUID id) {
        return jpaHubRouteLogStopRepository.findById(id)
                .filter(stop -> stop.getDeletedAt() == null);
    }

    @Override
    public void softDeleteByHubId(UUID hubId, UUID deletedBy) {
        jpaHubRouteLogStopRepository.softDeleteByHubId(hubId, deletedBy);
    }
}
