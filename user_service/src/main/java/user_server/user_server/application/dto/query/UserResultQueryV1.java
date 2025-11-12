package user_server.user_server.application.dto.query;

import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.User;

public record UserResultQueryV1 (

    String slackId,
    Role role,
    String username,
    String nickname,
    String email

){
    public static UserResultQueryV1 from(User user) {
        return new UserResultQueryV1(user.getSlackId(), user.getRole(), user.getUsername(), user.getNickname(), user.getEmail());
    }

}
