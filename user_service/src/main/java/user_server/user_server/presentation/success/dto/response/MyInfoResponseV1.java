package user_server.user_server.presentation.success.dto.response;

import lombok.Builder;
import user_server.user_server.application.dto.query.MyInfoQueryV1;
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
){
    public static MyInfoResponseV1 fromUserInfoResponse(MyInfoQueryV1 query) {
        return MyInfoResponseV1.builder()
            .slackId(query.slackId())
            .username(query.username())
            .nickname(query.nickname())
            .role(query.role())
            .point(query.point())
            .email(query.email())
            .is_public(query.is_public())
            .build();
    }

}
