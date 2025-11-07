package user_server.user_server.application.mapper;

import java.util.ArrayList;
import java.util.List;
import user_server.user_server.application.dto.command.SignupCommandV1;
import user_server.user_server.application.dto.command.UpdateMyInfoCommandV1;
import user_server.user_server.application.dto.command.UpdateUserInfoCommandV1;
import user_server.user_server.application.dto.query.MyInfoQueryV1;
import user_server.user_server.application.dto.query.UserInfoQueryV1;
import user_server.user_server.application.dto.query.UserListQueryV1;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.User;
import user_server.user_server.presentation.success.dto.request.SignupRequestV1;
import user_server.user_server.presentation.success.dto.request.UpdateMyInfoRequestV1;
import user_server.user_server.presentation.success.dto.request.UpdateUserInfoRequestV1;
import user_server.user_server.presentation.success.dto.response.MyInfoResponseV1;
import user_server.user_server.presentation.success.dto.response.UserInfoResponseV1;
import user_server.user_server.presentation.success.dto.response.UserListResponseV1;


public class UserMapper {

    public static List<UserListResponseV1> toUserListResponse(List<UserListQueryV1> userListQuery) {
        List<UserListResponseV1> userListResponse = new ArrayList<>();
        for (UserListQueryV1 query : userListQuery) {
            UserListResponseV1 userListResponseV1 = new UserListResponseV1(query.userId(), query.username());
            userListResponse.add(userListResponseV1);
        }
        return userListResponse;
    }

    public static List<UserListQueryV1> toUserListQuery(List<User> users) {
        List<UserListQueryV1> response = new ArrayList<>();
        for (User user : users) {
            UserListQueryV1 userListQuery = new UserListQueryV1(user.getId(), user.getUsername());
            response.add(userListQuery);
        }
        return response;
    }


    public static UserInfoQueryV1 toUserInfoQuery(User user) {
        return UserInfoQueryV1.builder()
            .userId(user.getId())
            .is_public(user.is_public())
            .deleteAt(user.getDeletedAt())
            .createAt(user.getCreatedAt())
            .username(user.getUsername())
            .email(user.getEmail())
            .signupStatus(user.getSignupStatus())
            .point(user.getPoint())
            .nickname(user.getNickname())
            .role(user.getRole())
            .slackId(user.getSlackId())
            .build();
    }

    public static UserInfoResponseV1 toUserInfoResponse(UserInfoQueryV1 query) {
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


    public static MyInfoQueryV1 toMyInfoQuery(User user) {
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

    public static MyInfoResponseV1 toUserInfoResponse(MyInfoQueryV1 query) {
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


    public static SignupCommandV1 toSignupCommandV1(SignupRequestV1 request) {
        return SignupCommandV1.builder()
            .slackId(request.slackId())
            .username(request.username())
            .email(request.email())
            .nickname(request.nickname())
            .password(request.password())
            .role(request.role())
            .build();
    }

    public static UpdateMyInfoCommandV1 toUpdateMyInfoCommandV1(UpdateMyInfoRequestV1 request) {
         return UpdateMyInfoCommandV1.builder()
            .slackId(request.slackId())
            .username(request.username())
            .email(request.email())
            .nickname(request.nickname())
            .role(request.role())
             .build();
    }

    public static UpdateUserInfoCommandV1 toUpdateUserInfoCommandV1(UpdateUserInfoRequestV1 request) {
        return UpdateUserInfoCommandV1.builder()
            .slackId(request.slackId())
            .email(request.email())
            .nickname(request.nickname())
            .username(request.username())
            .role(request.role())
            .point(request.point())
            .signupStatus(request.signupStatus())
            .build();
    }



}
