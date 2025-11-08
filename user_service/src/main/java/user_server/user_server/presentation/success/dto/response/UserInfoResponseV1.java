package user_server.user_server.presentation.success.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import user_server.user_server.application.dto.query.UserInfoQueryV1;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;

@Builder
public record UserInfoResponseV1(
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
    public static UserInfoResponseV1 fromUserInfoResponse(UserInfoQueryV1 query) {
        return UserInfoResponseV1.builder()
            .userId(query.userId())
            .is_public(query.is_public())
            .deleteAt(query.deleteAt())
            .createAt(query.createAt())
            .username(query.username())
            .email(query.email())
            .signupStatus(query.signupStatus())
            .point(query.point())
            .nickname(query.nickname())
            .role(query.role())
            .slackId(query.slackId())
            .build();
    }

}
