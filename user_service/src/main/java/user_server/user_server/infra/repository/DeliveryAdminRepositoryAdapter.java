package user_server.user_server.infra.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import user_server.user_server.domain.entity.DeliveryAdmin;
import user_server.user_server.domain.repository.DeliveryAdminRepository;
import user_server.user_server.domain.vo.UserRole;

@Repository
@RequiredArgsConstructor
public class DeliveryAdminRepositoryAdapter implements DeliveryAdminRepository {

    private final JpaDeliverAdminRepository jpaDeliverAdminRepository;

    @Override
    public void save(DeliveryAdmin admin) {
        jpaDeliverAdminRepository.save(admin);
    }


    @Override
    public long getHubUser(UUID hubId, UserRole userRole) {
        return jpaDeliverAdminRepository.countByHubIdAndUserRole(hubId, userRole);
    }

    @Override
    public Optional<DeliveryAdmin> readUser(String slackId) {
        return jpaDeliverAdminRepository.findBySlackId(slackId);
    }
}
