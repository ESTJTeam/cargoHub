package user_server.user_server.application.service;

import java.util.UUID;
import user_server.user_server.application.dto.query.UserResultQueryV1;
import user_server.user_server.infra.external.dto.response.UserResponseV1;

public interface UserClient {

    boolean deliveryAdminCreate(String slackId, UUID hubId, String userRole);

    UserResponseV1 getUser(UUID userId);

}
