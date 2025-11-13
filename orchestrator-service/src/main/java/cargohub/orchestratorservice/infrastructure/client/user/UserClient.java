package cargohub.orchestratorservice.infrastructure.client.user;

import cargohub.orchestratorservice.infrastructure.client.user.dto.response.UserResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/v1")
public interface UserClient {

    @GetMapping("/delivery-admin/user-info/{userId}")
    UserResponseV1 getUser(@PathVariable("userId") UUID userId);

}
