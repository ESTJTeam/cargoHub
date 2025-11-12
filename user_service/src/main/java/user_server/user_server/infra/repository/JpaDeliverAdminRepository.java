package user_server.user_server.infra.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user_server.user_server.domain.entity.DeliveryAdmin;
import user_server.user_server.domain.vo.UserRole;

@Repository
public interface JpaDeliverAdminRepository extends JpaRepository<DeliveryAdmin, UUID> {

    long countByHubIdAndUserRole(UUID hubId, UserRole userRole);


    Optional<DeliveryAdmin> findBySlackId(String slackId);
}
