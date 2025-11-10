package user_server.user_server.presentation.success.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import user_server.user_server.application.dto.query.UserListQueryV1;

public record UserListResponseV1(
    UUID userId,
    String username) {

    public static List<UserListResponseV1> fromUserListResponse(List<UserListQueryV1> userListQuery) {
        List<UserListResponseV1> userListResponse = new ArrayList<>();
        for (UserListQueryV1 query : userListQuery) {
            UserListResponseV1 userListResponseV1 = new UserListResponseV1(query.userId(), query.username());
            userListResponse.add(userListResponseV1);
        }
        return userListResponse;
    }
}
