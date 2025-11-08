package user_server.user_server.application.dto.query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import user_server.user_server.domain.entity.User;

public record UserListQueryV1 (
    UUID userId,
    String username
){
    public static List<UserListQueryV1> fromUserListQuery(List<User> users) {
        List<UserListQueryV1> response = new ArrayList<>();
        for (User user : users) {
            UserListQueryV1 userListQuery = new UserListQueryV1(user.getId(), user.getUsername());
            response.add(userListQuery);
        }
        return response;
    }

}
