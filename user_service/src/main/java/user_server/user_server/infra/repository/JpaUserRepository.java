package user_server.user_server.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>{

    @Query("SELECT u from User u where u.is_public = true and u.id =:userId and u.deletedAt IS NULL")
    Optional<User> findPublicById(@Param("userId") UUID userId);

    @Query("SELECT u from User u where u.is_public = true and u.username =:username and u.deletedAt IS NULL")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u from User u where u.is_public = true and u.username =:username and u.deletedAt IS NULL and u.email =:email")
    Optional<User> findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT u from User u where u.is_public = true and u.signupStatus =:status")
    List<User> findPendingUser(@Param("status") SignupStatus status);

    @Query("SELECT u from User u where u.id =:userId and u.role =:role")
    Optional<User> findMasterUser(@Param("userId") UUID userId, @Param("role") Role role);

    Optional<User> findBySlackId(String slackId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);
}

