package user_server.user_server.infra.external;

import java.util.UUID;
import lombok.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import user_server.user_server.application.dto.query.UserResultQueryV1;
import user_server.user_server.infra.external.dto.request.CreateDeliveryAdminRequestV1;
import user_server.user_server.presentation.success.dto.request.DeliveryAdminInfoRequestV1;

@FeignClient(name = "user-service", url = "${custom.service-url}")


public interface UserFeign {

    @PostMapping("/v1/delivery-admin")
    boolean createDeliveryAdmin(@RequestBody CreateDeliveryAdminRequestV1 createDeliveryAdminRequest);

    @GetMapping("/v1/delivery-admin/user-info/{userId}")
    UserResultQueryV1 readUserInfo(@PathVariable(name = "userId") UUID userId );

}
