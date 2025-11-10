package slack_service.application;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import slack_service.common.error.BusinessException;
import slack_service.common.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${spring.slack.bot-token}")
    private String slackBotToken;

    private static final String OPEN_CONVERSATION_URL = "https://slack.com/api/conversations.open";
    private static final String POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";

    private final RestClient restClient = RestClient.create();

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

        Map<String, Object> channel = (Map<String, Object>) response.get("channels");

        if (channel == null || channel.get("id") == null) {
            throw new BusinessException(ErrorCode.SLACK_CHANNEL_OPEN_FAILED);
        }

        return (String) channel.get("id"); // 예: DO7XYZ123
    }

    public void sendDmToUser(String userSlackId, String message) {

        // 1. DM 채널 생성 (또는 기존 DM 채널 가져오기)
        String channelId = openConversation(userSlackId);

        // 2. DM 전송
        restClient.post()
            .uri(POST_MESSAGE_URL)
            .header("Authorization", "Bearer " + slackBotToken)
            .header("Content-Type", "application/json")
            .body("""
                {
                  "channel": "%s",
                  "text": "%s"
                }
                """.formatted(channelId, message))
            .retrieve()
            .toBodilessEntity();
    }
}
