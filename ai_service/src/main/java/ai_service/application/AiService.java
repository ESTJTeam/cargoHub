package ai_service.application;

import ai_service.application.dto.request.AiDeadlineRequestV1;
import ai_service.application.dto.response.AiDeadlineResponseV1;
import ai_service.domain.entity.AiCallLog;
import ai_service.domain.repository.AiCallLogRepository;
import ai_service.infra.config.OpenAiConstants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        // 경유지 문자열 생성 (없으면 "없음")
        final String viaHubsText = (request.getViaHubs() == null || request.getViaHubs().isEmpty())
            ? "없음"
            : String.join(", ", request.getViaHubs());

        String prompt = OpenAiConstants.DEADLINE_SLACK_PROMPT.formatted(
            request.getOrderNum(),
            request.getRequesterName(),
            request.getRequesterEmail(),
            request.getOrderAt(),
            request.getProductName(),
            request.getQuantity(),
            request.getRequestNote(),
            request.getShipFromHub(),
            viaHubsText,
            request.getDestination(),
            request.getHandlerName(),
            request.getHandlerEmail()
        );

        String outputText = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content()
            .trim();

        // 전체 Slack 메시지 원본
        String slackFormattedText = outputText;

        // 줄 단위 분리
        String[] lines = outputText.split("\\R");

        // 마지막 줄 (발송 시한 안내 문구)
        String lastLine = lines[lines.length - 1].trim();

        // 마지막 줄을 제외한 나머지 → orderInfo
        String orderInfo = String.join("\n", Arrays.copyOf(lines, lines.length - 1)).trim();

        // finalDeadline 파싱
        LocalDateTime finalDeadline = null;
        Matcher matcher = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})").matcher(lastLine);
        if (matcher.find()) {
            finalDeadline = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        AiCallLog aiCallLog = AiCallLog.of(provider, model, prompt, outputText);

        aiCallLogRepository.save(aiCallLog);

        return AiDeadlineResponseV1.builder()
            .aiLogId(aiCallLog.getId())
            .orderInfo(orderInfo)
            .finalDeadline(finalDeadline)
            .slackFormattedText(slackFormattedText)
            .build();
    }
}

// TODO - 유저 받아오면 수정 작업 필요