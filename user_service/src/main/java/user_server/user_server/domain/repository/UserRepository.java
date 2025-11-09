package user_server.user_server.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import user_server.user_server.domain.entity.User;

public interface UserRepository {

    void save(User user);

    Optional<User> findPublicById(UUID userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndEmail(String username, String email);

    List<User> findAllUsers();

    List<User> findPendingUser();

    Optional<User> findByUserId(UUID userId);

    Optional<User> findMasterUser(UUID userId);

    Optional<User> findBySlackId(String slackId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String username);
}
