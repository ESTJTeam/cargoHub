package user_server.user_server.application.mapper;

import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.User;


public class UserMapper {

    public static User toUser(String slackId, String password, String username, Role role, String nickname, String email) {
        return User.builder()
            .username(username)
            .email(email)
            .nickname(nickname)
            .password(password)
            .role(role)
            .slackId(slackId).build();
    }

}
