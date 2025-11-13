package hub_server.hub_server.infra.repository;

import hub_server.hub_server.domain.entity.HubRouteLog;
import hub_server.hub_server.domain.repository.HubRouteLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteLogRepositoryAdaptor implements HubRouteLogRepository {

    private final JpaHubRouteLogRepository jpaHubRouteLogRepository;

    @Override
    public HubRouteLog save(HubRouteLog routeLog) {
        return jpaHubRouteLogRepository.save(routeLog);
    }

    @Override
    public Optional<HubRouteLog> findById(UUID id) {
        return jpaHubRouteLogRepository.findById(id)
                .filter(routeLog -> routeLog.getDeletedAt() == null);
    }

    @Override
    public List<HubRouteLog> findAllActive() {
        return jpaHubRouteLogRepository.findAllActive();
    }

    @Override
    public Optional<HubRouteLog> findByStartAndEndHubWithStops(UUID startHubId, UUID endHubId) {
        return jpaHubRouteLogRepository.findByStartAndEndHubWithStops(startHubId, endHubId);
    }

    @Override
    public Optional<HubRouteLog> findByStartAndEndHub(UUID startHubId, UUID endHubId) {
        return jpaHubRouteLogRepository.findByStartAndEndHub(startHubId, endHubId);
    }

    @Override
    public void softDeleteByHubId(UUID hubId, UUID deletedBy) {
        jpaHubRouteLogRepository.softDeleteByHubId(hubId, deletedBy);
    }

    @Override
    public void softDeleteAll(UUID deletedBy) {
        jpaHubRouteLogRepository.softDeleteAll(deletedBy);
    }

    @Override
    public void softDeleteAll(){

        jpaHubRouteLogRepository.softDeleteAll();
    }
}
