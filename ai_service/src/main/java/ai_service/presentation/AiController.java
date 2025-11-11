package ai_service.presentation;

import ai_service.application.AiService;
import ai_service.application.dto.request.AiDeadlineRequestV1;
import ai_service.application.dto.response.AiDeadlineResponseV1;
import ai_service.common.success.BaseResponse;
import ai_service.common.success.BaseStatus;
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

    /**
     * [AI 발송 시한 계산 - Prompt 데이터 직접 입력 버전]
     *
     * @param request AiDeadlineRequestV1 DTO
     * @return AI가 계산한 발송 시한과 Slack 메시지 원문, 주문 정보 요약을 담은 응답 DTO
     */
    @PostMapping("/deadline/prediction")
    public BaseResponse<AiDeadlineResponseV1> calculateDeadlineWithPromptData(
        @RequestBody AiDeadlineRequestV1 request) {

        AiDeadlineResponseV1 response = aiService.calculateDeadlineWithPromptData(request);

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }
}
