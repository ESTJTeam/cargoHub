package user_server.user_server.domain.repository;

import java.util.Optional;
import java.util.UUID;
import user_server.user_server.domain.entity.User;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(UUID userId);

    Optional<User> findBySlackId(String slackId);


}
