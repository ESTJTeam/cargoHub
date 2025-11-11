package ai_service.infra.client.order.response;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderForAiResponseV1 {

    private UUID orderNum;
    private String orderedAt;
    private String requestNote;
    private String shipFromHubId;
    private List<String> viaHubIds;
    private String destinationId;

    /*
     * 여기부터 프롬프트 출력용 (메시지에 그대로 사용)
     */

    // 주문자 정보
    private String requesterName;
    private String requesterEmail;
    // 상품 정보
    private String productName;
    private Integer quantity;
    // 배송 담당자 정보
    private String handlerName;
    private String handlerEmail;

    @Builder
    private OrderForAiResponseV1(
        UUID orderNum,
        String orderedAt,
        String requestNote,
        String shipFromHubId,
        List<String> viaHubIds,
        String destination,
        String requesterName,
        String requesterEmail,
        String productName,
        Integer quantity,
        String handlerName,
        String handlerEmail
    ) {
        this.orderNum = orderNum;
        this.orderedAt = orderedAt;
        this.requestNote = requestNote;
        this.shipFromHubId = shipFromHubId;
        this.viaHubIds = viaHubIds;
        this.destinationId = destination;
        this.requesterName = requesterName;
        this.requesterEmail = requesterEmail;
        this.productName = productName;
        this.quantity = quantity;
        this.handlerName = handlerName;
        this.handlerEmail = handlerEmail;
    }
}
