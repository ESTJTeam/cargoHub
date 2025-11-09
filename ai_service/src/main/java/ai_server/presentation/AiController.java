package ai_server.presentation;

import ai_server.application.AiService;
import ai_server.application.dto.request.AiDeadlineRequestV1;
import ai_server.application.dto.response.AiDeadlineResponseV1;
import ai_server.common.success.BaseResponse;
import ai_server.common.success.BaseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/generate-shipping-deadline-predictions")
    public BaseResponse<AiDeadlineResponseV1> generateShippingDeadlinePrediction(
        @RequestBody AiDeadlineRequestV1 request) {

        AiDeadlineResponseV1 response = aiService.generateShippingDeadlinePrediction(request);

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }
}
