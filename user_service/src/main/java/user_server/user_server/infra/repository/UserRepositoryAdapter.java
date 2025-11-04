package user_server.user_server.infra.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public Optional<User> findBySlackId(String slackId) {
        return jpaUserRepository.findBySlackId(slackId);
    }
}
