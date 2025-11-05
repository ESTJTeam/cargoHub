package user_server.user_server.domain.repository;

import java.util.Optional;
import java.util.UUID;
import user_server.user_server.domain.entity.User;

public interface UserRepository {

    void save(User user);

    Optional<User> findPublicById(UUID userId);

    Optional<User> findByUsername(String username);


}
