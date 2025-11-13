package ai_service.application;

import ai_service.domain.entity.AiCallLog;
import ai_service.domain.repository.AiCallLogRepository;
import ai_service.infra.config.OpenAiConstants;
import ai_service.presentation.request.CalculateAiDeadlineRequestV1;
import ai_service.presentation.response.AiDeadlineResponseV1;
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

    private static final DateTimeFormatter ORDER_AT_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * [AI 발송 시한 계산 - Prompt 데이터 직접 입력 버전]
     * 입력 받은 데이터를 기반으로 최종 발송 시한을 계산하고, Slack 메시지 형식의 텍스트를 반환
     * 발송 시한과 orderInfo를 파싱하여 Slack 전송용 데이터로 가공
     *
     * @param request CalculateAiDeadlineRequestV1 DTO (주문번호, 주문자 정보, 상품명, 수량, 출발 허브, 경유지, 목적지, 배송자 정보)
     * @return AI가 계산한 발송 시한과 Slack 메시지 원문, 주문 정보 요약을 담은 응답 DTO
     */
    @Transactional
    public AiDeadlineResponseV1 calculateDeadlineWithPromptData(
        CalculateAiDeadlineRequestV1 request) {

        final String provider = OpenAiConstants.PROVIDER_OPENAI;
        final String model = OpenAiConstants.MODEL_GPT_4O_MINI;

        // 경유지 문자열 생성 (없으면 "없음")
        final String viaHubsText = (request.getViaHubs() == null || request.getViaHubs().isEmpty())
            ? "없음"
            : String.join(", ", request.getViaHubs());

        // 주문 생성된 시간 파싱
        String orderAtText = request.getOrderAt().format(ORDER_AT_FMT);

        String prompt = OpenAiConstants.DEADLINE_SLACK_PROMPT.formatted(
            request.getOrderNum(),
            orderAtText,
            request.getProductName(),
            request.getQuantity(),
            request.getRequestNote(),
            request.getShipFromHub(),
            viaHubsText,
            request.getDestination()
        );

        String outputText = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content()
            .trim();

        // 줄 단위 분리
        String[] lines = outputText.split("\\R");

        // 마지막 줄을 제외한 나머지 → orderInfo
        String orderInfo = String.join("\n", Arrays.copyOf(lines, lines.length - 1)).trim();

        // 마지막 줄 (발송 시한 안내 문구)
        String lastLine = lines[lines.length - 1].trim();

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
            .build();
    }
}
