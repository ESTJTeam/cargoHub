package user_server.user_server.infra.external.dto.response;

import user_server.user_server.application.dto.query.UserResultQueryV1;
import user_server.user_server.domain.entity.Role;

public record UserResponseV1 (

    String slackId,
    Role role,
    String username,
    String nickname,
    String email

){
    public static UserResponseV1 from(UserResultQueryV1 query) {
        return new UserResponseV1(query.slackId(), query.role(), query.username(), query.nickname(), query.email());
    }

}
