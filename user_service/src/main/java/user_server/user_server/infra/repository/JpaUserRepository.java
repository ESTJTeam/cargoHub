package user_server.user_server.infra.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user_server.user_server.domain.entity.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>{

    Optional<User> findBySlackId(String slackId);

}
