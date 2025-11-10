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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 20, nullable = false)
    private String slackId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    private String username;    // 이게 사용자 ID라 함

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private boolean is_public; // 요구사항에서 is로 이름을 시작 함

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


    public User(String slackId, String password, String username, Role role, String nickname, String email) {
        this.slackId = slackId;
        this.password = password;
        this.is_public = true;
        this.email = email;
        this.nickname = nickname;
        this.username = username;
        this.signupStatus = SignupStatus.PENDING;
        this.point = 0;
        this.role = role;
    }

    public void deleteUser(UUID userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateSignupStatus(SignupStatus newStatus) {
        this.signupStatus = newStatus;
    }
    public void updateSlackId(String newSlackId) {this.slackId = newSlackId;}
    public void updateUsername(String username) {this.username = username;}
    public void updateNickname(String nickname) {this.nickname = nickname;}
    public void updateEmail(String email) {this.email = email;}
    public void updateRole(Role role) {this.role = role;}
    public void updatePoint(int point) {this.point = point;}


    /** 리프레시 토큰 제거(로그아웃) */
    public void clearRefreshToken() {
        this.refreshToken = null;
        this.refreshTokenExpiresAt = null;
    }

    public void updateRefreshToken(String hash, LocalDateTime expiresAt) {
        this.refreshToken = hash;
        this.refreshTokenExpiresAt = expiresAt;
    }

    public boolean isLoginAllowed(User user){
        return !user.getSignupStatus().equals(SignupStatus.PENDING) &&
            !user.getSignupStatus().equals(SignupStatus.REJECTED);
    }

    public static User createUser(String slackId, String password, String username, Role role, String nickname, String email) {
        return new User(slackId, password, username, role, nickname, email);
        }
    }
