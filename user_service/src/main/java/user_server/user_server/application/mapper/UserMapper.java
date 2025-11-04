package user_server.user_server.application.mapper;

import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;
import user_server.user_server.presentation.dto.request.SignupRequest;


public class UserMapper {

    public static User toUser(String name, String password, Role role, String slackId) {
        return User.builder()
            .name(name)
            .password(password)
            .role(role)
            .slackId(slackId).build();
    }

}
