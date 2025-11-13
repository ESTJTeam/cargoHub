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
import user_server.user_server.application.dto.command.NewNumDeliveryAdminCommandV1;
import user_server.user_server.application.dto.query.CreateDeliveryAdminQueryV1;
import user_server.user_server.application.dto.query.ReadDeliveryAdminQueryV1;
import user_server.user_server.infra.external.dto.request.NewNumDeliveryAdminRequestV1;
import user_server.user_server.infra.external.dto.response.NewDeliveryAdminResponseV1;
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
        return deliveryAdminService.create(request.toDeliveryAminCommand());
    }


    @GetMapping("/delivery-admin/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryAdminResponseV1 readDeliverAdmin(@PathVariable("userId") UUID userId){
        CreateDeliveryAdminQueryV1 deliveryAdminQuery = deliveryAdminService.readDeliveryAdmin(
            new DeliveryAdminInfoRequestV1(userId));

        return DeliveryAdminResponseV1.from(deliveryAdminQuery);
    }
    /*  배송 담당자 유저에 대한 정보
    "userId": "2e0e759f-a7eb-4278-b325-9d29",
    "hubId": "3fa85f64-5717-4562-b3fc-2c9afa6",
    "slackId": "aaaaaaaaaaa",
    "userRole": "SUPPLIER_MANAGER",
    "deliverySequenceNum": 1
    */


    @GetMapping("/delivery-admin/user-info/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseV1 readMyInfo(@PathVariable("userId")UUID userId) {
        return UserResponseV1.from(userService.readUser(new ReadDeliveryAdminQueryV1(userId)));
    }
    /* 유저에 대한 정보 (내부 통신용?)
    "slackId": "aaaaaaaaaaa",
    "role": "DELIVERY_MANAGER",
    "username": "test",
    "nickname": "test",
    "email": "test@example.com" */


    @PostMapping("/delivery-admin/new-man")
    @ResponseStatus(HttpStatus.OK)
    public NewDeliveryAdminResponseV1 newDeliveryAdmin(@RequestBody @Valid NewNumDeliveryAdminRequestV1 request){
        return deliveryAdminService.newNumDeliverAdmin(NewNumDeliveryAdminCommandV1.from(request));

    }

}
