package cargohub.orchestratorservice.presentation;

import cargohub.orchestratorservice.application.OrchestratorOrderService;
import cargohub.orchestratorservice.infrastructure.dto.CreateOrderRequestV1;
import cargohub.orchestratorservice.presentation.success.dto.BaseResponse;
import cargohub.orchestratorservice.presentation.success.dto.BaseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orchestrations/orders")
@Slf4j
@RequiredArgsConstructor
public class OrchestratorController {

    private final OrchestratorOrderService orchestratorOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createOrder(@Valid @RequestBody CreateOrderRequestV1 request){


        log.info("주문 생성 API 호출됨. 요청값 = {}", request);

        orchestratorOrderService.createOrder(request);

        // id값 보기 가능하게 log로 id
        return BaseResponse.ok(BaseStatus.CREATED);
    }
}
