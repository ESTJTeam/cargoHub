package user_server.user_server.infra.external;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubFeign {

    @GetMapping("/v1/hubs/{hubId]")
    boolean validateHub(@PathVariable("hubId") UUID hubId);



}
