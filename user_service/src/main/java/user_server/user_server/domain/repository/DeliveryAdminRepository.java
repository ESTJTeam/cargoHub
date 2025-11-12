package user_server.user_server.domain.repository;

import java.util.Optional;
import java.util.UUID;
import user_server.user_server.domain.entity.DeliveryAdmin;
import user_server.user_server.domain.vo.UserRole;

public interface DeliveryAdminRepository {

    void save(DeliveryAdmin admin);

    long getHubUser(UUID hubId, UserRole userRole);

    Optional<DeliveryAdmin> readUser(String slackId);
}
