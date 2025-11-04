package user_server.user_server.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import user_server.user_server.global.unit.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 20, nullable = false)
    private String slackId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SignupStatus signupStatus;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private LocalDateTime refreshTokenExpiresAt;

    // TODO 소속 업체나 허브 필요


// 배송원이나 업체 사람이 가입하는 형태?
    @Builder
    public User(String slackId, String password, String name, Role role) {
        this.slackId = slackId;
        this.password = password;
        this.name = name;
        this.signupStatus = SignupStatus.PENDING;
        this.point = 0;
        this.role = role;
    }

    public void deleteUser(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateSignupStatus(SignupStatus newStatus) {
        this.signupStatus = newStatus;
    }

    public void updateRole(Role role) {this.role = role;}

    /** 만료 여부 */
    public boolean isRefreshExpired(LocalDateTime now) {
        return refreshTokenExpiresAt == null || now.isAfter(refreshTokenExpiresAt);
    }

    /** 리프레시 토큰 제거(로그아웃) */
    public void clearRefreshToken() {
        this.refreshToken = null;
        this.refreshTokenExpiresAt = null;
    }

    public void updateRefreshToken(String hash, LocalDateTime expiresAt) {
        this.refreshToken = hash;
        this.refreshTokenExpiresAt = expiresAt;
    }




}