package user_server.user_server.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.command.DeliveryAminCommandV1;
import user_server.user_server.application.dto.command.NewNumDeliveryAdminCommandV1;
import user_server.user_server.application.dto.query.CreateDeliveryAdminQueryV1;
import user_server.user_server.domain.entity.DeliveryAdmin;
import user_server.user_server.domain.repository.DeliveryAdminRepository;
import user_server.user_server.infra.external.HubClientImpl;
import user_server.user_server.infra.external.UserClientImpl;
import user_server.user_server.infra.external.dto.response.NewDeliveryAdminResponseV1;
import user_server.user_server.infra.external.dto.response.UserResponseV1;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;
import user_server.user_server.presentation.success.dto.request.DeliveryAdminInfoRequestV1;

@Service
@RequiredArgsConstructor
public class DeliveryAdminService {

    private final DeliveryAdminRepository deliveryAdminRepository;
    private final UserClientImpl userClient;
    private final HubClientImpl hubClient;


    @Transactional
    public boolean create(DeliveryAminCommandV1 deliveryAminCommand) {

        // 허브 검증 서비스
        //hubClient.validateHub(deliveryAminCommand.hubId());

        long userCount = deliveryAdminRepository.getHubUser(deliveryAminCommand.hubId(),
            deliveryAminCommand.userRole());
        int count = (int) (userCount + 1);

        if (count>=11){
            throw new BusinessException(ErrorCode.TOO_MANY_DELIVERY_MANAGERS);
        }
        // 시간이 모잘라 동시성 작업을 못 함
        DeliveryAdmin deliveryAdmin = DeliveryAdmin.create(deliveryAminCommand.SlackId(),
            deliveryAminCommand.hubId(), deliveryAminCommand.userRole(), count);
        deliveryAdminRepository.save(deliveryAdmin);
        return true;
    }



    @Transactional(readOnly = true)
    public CreateDeliveryAdminQueryV1 readDeliveryAdmin(DeliveryAdminInfoRequestV1 deliveryAdminInfoRequest) {

        // 유저 정보 조회
        UserResponseV1 user = userClient.getUser(deliveryAdminInfoRequest.userId());
        DeliveryAdmin deliveryAdmin = deliveryAdminRepository.readUser(user.slackId())
            .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new CreateDeliveryAdminQueryV1(deliveryAdminInfoRequest.userId(), deliveryAdmin.getSlackId(),deliveryAdmin.getHubId(),
            deliveryAdmin.getUserRole(), deliveryAdmin.getDeliverySequenceNum());
    }




    @Transactional
    public NewDeliveryAdminResponseV1 newNumDeliverAdmin(NewNumDeliveryAdminCommandV1 command) {
        deliveryAdminRepository.readAllHubRoleUser(command.hubId(), command.userRole());




    }
}
