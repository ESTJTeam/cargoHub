package user_server.user_server.application.dto.query;

import lombok.Builder;
import user_server.user_server.domain.entity.Role;

@Builder
public record MyInfoQueryV1 (
    String slackId,
    String username,
    String nickname,
    String email,
    boolean is_public,
    int point,
    Role role
){}
