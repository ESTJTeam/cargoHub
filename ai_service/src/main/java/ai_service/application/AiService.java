package ai_service.application;

import ai_service.domain.entity.AiCallLog;
import ai_service.domain.repository.AiCallLogRepository;
import ai_service.infra.client.hub.HubClient;
import ai_service.infra.client.hub.response.HubAddressForAiResponseV1;
import ai_service.infra.client.order.OrderClient;
import ai_service.infra.client.order.response.OrderForAiResponseV1;
import ai_service.infra.config.OpenAiConstants;
import ai_service.presentation.request.CalculateAiDeadlineRequestV1;
import ai_service.presentation.response.AiDeadlineResponseV1;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    private final OrderClient orderClient;
    private final HubClient hubClient;

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

        String orderAtText = request.getOrderAt().format(ORDER_AT_FMT);

        String prompt = OpenAiConstants.DEADLINE_SLACK_PROMPT.formatted(
            request.getOrderNum(),
            request.getRequesterName(),
            request.getRequesterEmail(),
            orderAtText,
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

    /**
     * [AI 발송 시한 계산 - 주문번호 기반 자동 생성 버전]
     * AI 프롬프트에 필요한 주문&배송 정보를 자동으로 구성해서 최종 발송 시한 계산
     *
     * @param request 발송 시한 계산 대상 Order 정보 담은 DTO
     * @return AI가 계산한 발송 시한과 Slack 메시지 원문, 주문 요약이 포함된 응답 DTO
     */
    @Transactional
    public AiDeadlineResponseV1 generateDeadlineByOrder(CalculateAiDeadlineRequestV1 request) {

        // 1. Order 서비스에서 AI 발송시한 계산 위한 Order 정보 수집
        OrderForAiResponseV1 order = orderClient.getOrderForAi(request.getOrderNum());

        // 2. 허브 주소 UUID 리스트 수집 (shipFromHub, viaHubs, destination)
        List<UUID> addressIdList = new ArrayList<>();

        // shipFromHub UUID 리스트에 추가
        UUID shipFromHubAddressId = parseUuidSafe(order.getShipFromHubId());
        if (shipFromHubAddressId != null) {
            addressIdList.add(shipFromHubAddressId);
        }

        // destination UUID 리스트에 추가
        UUID destinationAddressId = parseUuidSafe(order.getDestinationId());
        if (destinationAddressId != null) {
            addressIdList.add(destinationAddressId);
        }

        // viaHubs UUID 리스트에 추가
        if (order.getViaHubIds() != null) {
            for (String viaHubId : order.getViaHubIds()) {
                UUID id = parseUuidSafe(viaHubId);
                if (id != null) {
                    addressIdList.add(id);
                }
            }
        }

        // 3. Hub 서비스에서 주소 일괄 조회
        List<HubAddressForAiResponseV1> hubAddressResponseList = addressIdList.isEmpty()
            ? List.of()
            : hubClient.getAddresses(addressIdList);

        // 주소 목록을 주소ID → 주소문자열 형태로 변환할 Map 구성
        Map<UUID, String> addressMap = new LinkedHashMap<>();

        if (hubAddressResponseList != null) {
            for (HubAddressForAiResponseV1 address : hubAddressResponseList) {
                addressMap.put(address.getAddressId(), safe(address.getFormattedAddress()));
            }
        }

        // 출발지 주소 문자열로 매핑
        String shipFromHubAddress = addressMap.get(shipFromHubAddressId);

        // 경유지 주소 문자열로 매핑
        List<String> viaHubAddressList = new ArrayList<>();
        if (order.getViaHubIds() != null) {
            for (String viaHubId : order.getViaHubIds()) {
                UUID id = parseUuidSafe(viaHubId);
                if (id != null) {
                    viaHubAddressList.add(addressMap.get(id));
                }
            }
        }

        // 도착지 주소 문자열로 매핑
        String destination = addressMap.get(destinationAddressId);

        // 4. 요청 DTO로 변환
        CalculateAiDeadlineRequestV1 aiDeadlineRequest = CalculateAiDeadlineRequestV1.builder()
            .orderNum(order.getOrderNum())
            .requesterName(safe(order.getRequesterName()))
            .requesterEmail(safe(order.getRequesterEmail()))
            .orderAt(parseLocalDateTimeSafe(order.getOrderedAt()))
            .productName(safe(order.getProductName()))
            .quantity(order.getQuantity() == null ? 0 : order.getQuantity())
            .requestNote(safe(order.getRequestNote()))
            .shipFromHub(safe(shipFromHubAddress))
            .viaHubs(viaHubAddressList)
            .destination(safe(destination))
            .handlerName(safe(order.getHandlerName()))
            .handlerEmail(safe(order.getHandlerEmail()))
            .build();

        // 5. 프롬프트 파싱
        return calculateDeadlineWithPromptData(aiDeadlineRequest);
    }

    /* [문자열을 UUID로 안전하게 변환하는 유틸 메서드]
     *
     * 입력 문자열이 null, 공백, 또는 UUID 형식이 아닐 경우 예외를 던지지 않고 null을 반환
     * 유효한 UUID 문자열일 때만 UUID 객체로 변환
     */
    private static UUID parseUuidSafe(String s) {

        if (s == null || s.isBlank()) {
            return null;
        }

        try {
            return UUID.fromString(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /* [문자열을 null-safe 처리하는 유틸 메서드]
     *
     * null 이면 빈 문자열("")을 반환, 아니면 그대로 반환
     * 템플릿 포맷팅(prompt 작성 등)에서 null 값으로 인한 NPE 방지하는 용도로 사용
     */
    private static String safe(String s) {

        return s == null ? "" : s;
    }

    /* [문자열을 LocalDateTime으로 안전하게 변환하는 유틸 메서드]
     *
     * 입력 문자열이 null, 공백, 또는 형식 불일치일 경우 예외를 던지지 않고 null을 반환
     */
    private static LocalDateTime parseLocalDateTimeSafe(String s) {

        // @NotNull에 걸리도록 컨트롤러/서비스 레벨 검증 권장
        if (s == null || s.isBlank()) return null;

        return LocalDateTime.parse(s.trim(), ORDER_AT_FMT);
    }
}
