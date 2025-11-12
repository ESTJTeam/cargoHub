package user_server.user_server.presentation;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import user_server.user_server.application.DeliveryAdminService;
import user_server.user_server.application.UserService;
import user_server.user_server.application.dto.query.DeliveryAdminQueryV1;
import user_server.user_server.domain.vo.UserRole;
import user_server.user_server.infra.external.dto.response.UserResponseV1;
import user_server.user_server.presentation.success.dto.request.DeliverAdminRequestV1;
import user_server.user_server.presentation.success.dto.request.DeliveryAdminInfoRequestV1;
import user_server.user_server.presentation.success.dto.response.DeliveryAdminResponseV1;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeliveryAdminController {

    private final DeliveryAdminService deliveryAdminService;
    private final UserService userService;

    @PostMapping("/delivery-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createDeliveryAdmin(@RequestBody @Valid DeliverAdminRequestV1 request) {
        // TODO 허브 검증
        return deliveryAdminService.create(request.toDeliveryAminCommand());
    }


//    @GetMapping("/delivery-admin")
//    @ResponseStatus(HttpStatus.OK)
//    // 해당 허브
//    public DeliveryAdminResponseV1 readDeliverAdmin(@PathVariable("userRole")UserRole userRole) {
//        DeliveryAdminQueryV1 deliveryAdminQuery = deliveryAdminService.readDeliveryAdmin(
//            new DeliveryAdminInfoRequestV1();
//        return DeliveryAdminResponseV1.from(deliveryAdminQuery);
//    }


    @GetMapping("/delivery-admin/user-info/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseV1 readMyInfo(@PathVariable("userId")UUID userId) {
        return UserResponseV1.from(userService.readUser(userId));
    }
    /* 유저에 대한 정보 (내부 통신용?)
    "slackId": "aaaaaaaaaaa",
    "role": "DELIVERY_MANAGER",
    "username": "test",
    "nickname": "test",
    "email": "test@example.com" */

}
