package user_server.user_server.application.dto.query;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;

@Builder
public record UserInfoQueryV1 (
    UUID userId,
    String slackId,
    String username,
    String nickname,
    String email,
    boolean is_public,
    Role role,
    SignupStatus signupStatus,
    int point,
    LocalDateTime createAt,
    LocalDateTime deleteAt
    // 허브나 업체 ID 예정
){
    public static UserInfoQueryV1 fromUserInfoQuery(User user) {
        return UserInfoQueryV1.builder()
            .userId(user.getId())
            .is_public(user.is_public())
            .deleteAt(user.getDeletedAt())
            .createAt(user.getCreatedAt())
            .username(user.getUsername())
            .email(user.getEmail())
            .signupStatus(user.getSignupStatus())
            .point(user.getPoint())
            .nickname(user.getNickname())
            .role(user.getRole())
            .slackId(user.getSlackId())
            .build();
    }

}
