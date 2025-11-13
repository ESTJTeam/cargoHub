//package ai_service.infra.client.hub;
//
//import ai_service.infra.client.hub.response.HubAddressForAiResponseV1;
//import java.util.List;
//import java.util.UUID;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(
//    name = "hubClient",
//    url = "${clients.hub.url}",
//    configuration = ai_service.infra.config.FeignConfig.class
//)
//public interface HubClient {
//
//    @GetMapping("/v1/hubs/addresses")
//    List<HubAddressForAiResponseV1> getAddresses(@RequestParam("ids") List<UUID> addressIds);
//}

/* TODO
 * 주석 처리된 미사용 코드 제거
 */