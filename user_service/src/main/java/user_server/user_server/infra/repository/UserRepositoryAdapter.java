package user_server.user_server.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findBySlackId(String slackId) {
        return jpaUserRepository.findBySlackId(slackId);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return jpaUserRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findPublicById(UUID userId) {
        return jpaUserRepository.findPublicById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameAndEmail(String username, String email) {
        return jpaUserRepository.findByUsernameAndEmail(username, email);
    }

    @Override
    public List<User> findAllUsers() {
        return jpaUserRepository.findAll();
    }

    @Override
    public List<User> findPendingUser() {
        return jpaUserRepository.findPendingUser(SignupStatus.PENDING);
    }

    @Override
    public Optional<User> findByUserId(UUID userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public Optional<User> findMasterUser(UUID userId) {
        return jpaUserRepository.findMasterUser(userId, Role.MASTER);
    }

}
