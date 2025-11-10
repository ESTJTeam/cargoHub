package slack_service.application;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import slack_service.application.dto.request.SlackDeadlineRequestV1;
import slack_service.application.dto.request.SlackMessageRequestV1;
import slack_service.common.error.BusinessException;
import slack_service.common.error.ErrorCode;
import slack_service.domain.entity.SlackLog;
import slack_service.domain.repository.SlackLogRepository;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${spring.slack.bot-token}")
    private String slackBotToken;

    private static final String OPEN_CONVERSATION_URL = "https://slack.com/api/conversations.open";
    private static final String POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DEADLINE_FORMAT = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    private final RestClient restClient = RestClient.create();
    private final SlackLogRepository slackLogRepository;

    /**
     * [Slack DM 채널 생성]
     * 특정 사용자(userSlackId)와의 1:1 DM 채널을 개설하거나 기존 채널을 반환한다.
     *
     * @param userSlackId 메시지 수신자의 Slack 아이디
     * @return 채널 아이디
     */
    private String openConversation(String userSlackId) {

        var response = restClient.post()
            .uri(OPEN_CONVERSATION_URL)
            .header("Authorization", "Bearer " + slackBotToken)
            .header("Content-Type", "application/json")
            .body("""
                { "users": ["%s"] }
                """.formatted(userSlackId))
            .retrieve()
            .body(Map.class);

        if (response == null || Boolean.FALSE.equals(response.get("ok"))) {
            throw new BusinessException(ErrorCode.SLACK_CHANNEL_OPEN_FAILED);
        }

        Map<String, Object> channel = (Map<String, Object>) response.get("channel");

        if (channel == null || channel.get("id") == null) {
            throw new BusinessException(ErrorCode.SLACK_CHANNEL_OPEN_FAILED);
        }

        return (String) channel.get("id"); // 예: DO7XYZ123
    }

    /**
     * [Slack 메시지 전송 - 모든 로그인한 유저 가능]
     * 특정 사용자와의 DM 채널을 열고 입력한 메시지를 전송한다.
     *
     * @param receiverSlackId 메시지 수신자의 Slack 아이디
     * @param message         전송할 메시지 본문
     */
    public void sendDmToUser(String receiverSlackId, String message) {

        // 1. DM 채널 생성 (또는 기존 DM 채널 가져오기)
        String channelId = openConversation(receiverSlackId);

        SlackMessageRequestV1 request = SlackMessageRequestV1.builder()
            .channel(channelId)
            .text(message)
            .build();

        // 2. DM 전송
        restClient.post()
            .uri(POST_MESSAGE_URL)
            .header("Authorization", "Bearer " + slackBotToken)
            .header("Content-Type", "application/json")
            .body(request)
            .retrieve()
            .toBodilessEntity();

        SlackLog slackLog = SlackLog.of(receiverSlackId, message);

        slackLogRepository.save(slackLog);
    }

    /**
     * [Ai 연동 - 최종 발송 시한 메시지 자동 전송]
     * AI가 생성한 Slack 메시지를 담당자에게 자동으로 전송한다.
     * slackFormattedText 있으면 그대로 전송
     * 없으면 orderInfo + finalDeadline 으로 폴백 메시지 생성 후 전송
     *
     * @param request AI 메시지 또는 Fallback 데이터
     */
    public void sendDeadlineNotice(SlackDeadlineRequestV1 request) {

        if (!StringUtils.hasText(request.getReceiverSlackId())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }

        // 1. AI가 생성한 메시지가 존재할 경우 그대로 사용
        String text = request.getSlackFormattedText();

        // 2. 없을 경우 Fallback 메시지 구성
        if (!StringUtils.hasText(text)) {

            // 필수 정보(orderInfo, finalDeadline) 없으면 예외
            if (!StringUtils.hasText(request.getOrderInfo()) || request.getFinalDeadline() == null) {
                throw new BusinessException(ErrorCode.INVALID_PARAMETER);
            }

            text = buildFallbackDeadlineText(
                request.getOrderInfo(),
                request.getFinalDeadline(),
                request.getAiLogId()
            );
        }

        // 3. 메시지 하단에 aiLogId 메타 정보 추가
        text = appendMetaLine(text, request.getAiLogId());

        sendDmToUser(request.getReceiverSlackId(), text);
    }

    // Fallback 텍스트 생성
    private String buildFallbackDeadlineText(String orderInfo, LocalDateTime finalDeadline,
        UUID aiLogId) {

        // 시스템 시간대 → 한국 시간대(KST) 변환 및 포맷팅
        String deadlineKst = finalDeadline.atZone(ZoneId.systemDefault())
            .withZoneSameInstant(KST)
            .toLocalDateTime()
            .format(DEADLINE_FORMAT);

        String body = """
            *배송 최종 발송 시한 알림*
            • 주문: %s
            • 최종 발송 시한(KST): %s
            """.formatted(orderInfo, deadlineKst);

        return appendMetaLine(body, aiLogId);
    }

    // aiLogId 추적 용도
    private String appendMetaLine(String text, UUID aiLogId) {

        if (aiLogId == null) {
            return text;
        }

        // Slack 마크다운의 구분선 대용으로 공백 줄 + 얇은 meta 라인 추가
        return text + "\n\n— _aiLogId: " + aiLogId + "_";
    }
}
