package user_server.user_server.infra.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user_server.user_server.domain.entity.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>{

    @Query("SELECT u from User u where u.is_public = true and u.id =:userId")
    Optional<User> findPublicById(@Param("userId") UUID userId);


    @Query("SELECT u from User u where u.is_public = true and u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

}
