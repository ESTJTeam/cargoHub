package ai_service.presentation;

import ai_service.application.AiService;
import ai_service.common.success.BaseResponse;
import ai_service.common.success.BaseStatus;
import ai_service.presentation.request.CalculateAiDeadlineRequestV1;
import ai_service.presentation.response.AiDeadlineResponseV1;
import jakarta.validation.Valid;
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
     * @param request CalculateAiDeadlineRequestV1 DTO
     * @return AI가 계산한 발송 시한과 Slack 메시지 원문, 주문 정보 요약을 담은 응답 DTO
     */
    @PostMapping("/deadline/prediction")
    public BaseResponse<AiDeadlineResponseV1> calculateDeadlineWithPromptData(
        @RequestBody CalculateAiDeadlineRequestV1 request) {

        AiDeadlineResponseV1 response = aiService.calculateDeadlineWithPromptData(request);

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }

    /**
     * [최종 발송 시한 계산 - 주문번호 기반 자동 생성 버전]
     *
     * @param request 발송 시한 계산 대상 Order 정보 담은 DTO
     * @return AI가 계산한 발송 시한과 Slack 메시지 원문, 주문 요약이 포함된 응답 DTO
     */
    @PostMapping("/deadline/{orderId}")
    public AiDeadlineResponseV1 generateDeadlineByOrder(@RequestBody @Valid CalculateAiDeadlineRequestV1 request) {

        // 내부에서 Order+Hub 조회 → 프롬프트 생성 → AI 계산
        return aiService.generateDeadlineByOrder(request);
    }
}
