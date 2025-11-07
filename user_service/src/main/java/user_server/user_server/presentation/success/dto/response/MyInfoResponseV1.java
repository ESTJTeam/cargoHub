package user_server.user_server.presentation.success.dto.response;

import lombok.Builder;
import user_server.user_server.domain.entity.Role;

@Builder
public record MyInfoResponseV1 (
    String slackId,
    String username,
    String nickname,
    String email,
    boolean is_public,
    int point,
    Role role
){}
