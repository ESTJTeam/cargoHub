package user_server.user_server.application.dto.query;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;

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
){}
