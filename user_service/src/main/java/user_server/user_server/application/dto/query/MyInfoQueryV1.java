package user_server.user_server.application.dto.query;

import lombok.Builder;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.User;

@Builder
public record MyInfoQueryV1 (
    String slackId,
    String username,
    String nickname,
    String email,
    boolean is_public,
    int point,
    Role role
){
    public static MyInfoQueryV1 fromMyInfoQuery(User user) {
        return MyInfoQueryV1.builder()
            .slackId(user.getSlackId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .role(user.getRole())
            .point(user.getPoint())
            .email(user.getEmail())
            .is_public(user.is_public())
            .build();
    }

}
