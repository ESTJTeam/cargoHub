package user_server.user_server.infra.external;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import user_server.user_server.application.service.UserClient;
import user_server.user_server.infra.external.dto.request.CreateDeliveryAdminRequestV1;
import user_server.user_server.infra.external.dto.response.UserResponseV1;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final UserFeign userFeign;


    @Override
    public boolean deliveryAdminCreate(String slackId, UUID hubId, String userRole) {
         return userFeign.createDeliveryAdmin(new CreateDeliveryAdminRequestV1(slackId, hubId, userRole));
    }

    @Override
    public UserResponseV1 getUser(UUID userId) {
        return UserResponseV1.from(userFeign.readUserInfo(userId));
    }
}
