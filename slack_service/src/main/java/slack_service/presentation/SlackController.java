package slack_service.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slack_service.application.SlackService;
import slack_service.application.dto.request.SlackDeadlineRequestV1;
import slack_service.application.dto.request.SlackMessageRequestV1;
import slack_service.common.success.BaseResponse;
import slack_service.common.success.BaseStatus;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/slack")
public class SlackController {

    private final SlackService slackService;

    /**
     * [Slack 사용자에게 DM 전송 - 모든 로그인한 유저 가능]
     * Slack API 통해 메시지를 전송
     *
     * @param request "channel": "userSlackId", "text": "메시지 내용"
     * @return status CREATED 반환
     */
    @PostMapping("/post-message")
    public BaseResponse<Void> postMessage(@RequestBody SlackMessageRequestV1 request) {

        slackService.sendDmToUser(request.getChannel(), request.getText());

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    /**
     * [AI 발송 시한 안내 메시지 자동 전송]
     * AI로 계산된 최종 발송 시한 정보를 Slack API 통해 발송하는 허브 담당자에개 전송
     *
     * @param request "receiverSlackId": "대상자 Slack ID",
     *                "slackFormattedText": "AI가 생성한 전체 메시지(있을 경우 그대로 전송)",
     *                "orderInfo": "주문 기본 정보(Fallback 용도)",
     *                "finalDeadline": "최종 발송 시한",
     *                "aiLogId": "AI 로그 추적용 UUID"
     * @return status CREATED 반환
     */
    @PostMapping("/send-deadline-notice")
    public BaseResponse<Void> sendDeadlineNotice(@Valid @RequestBody SlackDeadlineRequestV1 request) {

        slackService.sendDeadlineNotice(request);

        return BaseResponse.ok(BaseStatus.CREATED);
    }
}
