package ai_server.application;

import ai_server.application.dto.request.AiDeadlineRequestV1;
import ai_server.application.dto.response.AiDeadlineResponseV1;
import ai_server.domain.entity.AiCallLog;
import ai_server.domain.repository.AiCallLogRepository;
import ai_server.infra.config.OpenAiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final AiCallLogRepository aiCallLogRepository;

    @Transactional
    public AiDeadlineResponseV1 generateShippingDeadlinePrediction(
        AiDeadlineRequestV1 request) {

        final String provider = OpenAiConstants.PROVIDER_OPENAI;
        final String model = OpenAiConstants.MODEL_GPT_4O_MINI;

        String prompt = OpenAiConstants.DEADLINE_SLACK_PROMPT.formatted(
            request.getOrderNo(),
            request.getRequesterName(),
            request.getRequesterEmail(),
            request.getOrderAt(),
            request.getProductName(),
            request.getQuantity(),
            request.getRequestNote(),
            request.getShipFromHub(),
            String.join(" ", request.getShipFromHub()),
            request.getDestination(),
            request.getHandlerName(),
            request.getHandlerEmail()
        );

        String outputText = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();

        // 마지막 줄: "최종 발송 시한은 …"
        String slackFormattedText = outputText.lines().reduce((a, b) -> b).orElse(outputText);

        AiCallLog aiCallLog = AiCallLog.of(
            provider,
            model,
            prompt,
            outputText
        );

        aiCallLogRepository.save(aiCallLog);

        // TODO - finalDeadlineAt 파싱 추가 시 수정
        return AiDeadlineResponseV1.builder()
            .aiLogId(aiCallLog.getId())
            .finalDeadlineText(slackFormattedText)
            .finalDeadline(null)
            .slackFormattedText(outputText)
            .build();
    }
}
