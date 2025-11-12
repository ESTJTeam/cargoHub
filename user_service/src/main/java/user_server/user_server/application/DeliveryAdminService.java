package user_server.user_server.application;

import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.command.DeliveryAminCommandV1;
import user_server.user_server.application.dto.query.DeliveryAdminQueryV1;
import user_server.user_server.application.dto.query.UserResultQueryV1;
import user_server.user_server.domain.entity.DeliveryAdmin;
import user_server.user_server.domain.repository.DeliveryAdminRepository;
import user_server.user_server.infra.external.UserClientImpl;
import user_server.user_server.infra.external.dto.response.UserResponseV1;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;
import user_server.user_server.presentation.success.dto.request.DeliveryAdminInfoRequestV1;

@Service
@RequiredArgsConstructor
public class DeliveryAdminService {

    private final DeliveryAdminRepository deliveryAdminRepository;
    private final UserClientImpl userClient;


    @Transactional
    public boolean create(DeliveryAminCommandV1 deliveryAminCommand) {

        long userCount = deliveryAdminRepository.getHubUser(deliveryAminCommand.hubId(),
            deliveryAminCommand.userRole());
        int count = (int) (userCount + 1);

        if (count>=11){
            throw new BusinessException(ErrorCode.TOO_MANY_DELIVERY_MANAGERS);
        }

        DeliveryAdmin deliveryAdmin = DeliveryAdmin.create(deliveryAminCommand.SlackId(),
            deliveryAminCommand.hubId(), deliveryAminCommand.userRole(), count);

        deliveryAdminRepository.save(deliveryAdmin);
        return true;
    }



//    @Transactional(readOnly = true)
//    public DeliveryAdminQueryV1 readDeliveryAdmin(DeliveryAdminInfoRequestV1 deliveryAdminInfoRequest) {
//
//        // 유저 정보 조회
//        UserResponseV1 user = userClient.getUser(deliveryAdminInfoRequest.userId());
//
//        DeliveryAdmin deliveryAdmin = deliveryAdminRepository.readUser(user.slackId())
//            .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
//
//        return new DeliveryAdminQueryV1(deliveryAdmin.getSlackId(),deliveryAdmin.getHubId(),
//            deliveryAdmin.getUserRole(), deliveryAdmin.getDeliverySequenceNum());
//
//    }
}
