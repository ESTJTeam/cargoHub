package cargohub.orchestratorservice.application;

import cargohub.orchestratorservice.infrastructure.client.ai.AiClient;
import cargohub.orchestratorservice.infrastructure.client.ai.dto.request.CalculateAiDeadlineRequestV1;
import cargohub.orchestratorservice.infrastructure.client.ai.dto.response.AiDeadlineResponseV1;
import cargohub.orchestratorservice.infrastructure.client.firm.FirmClient;
import cargohub.orchestratorservice.infrastructure.client.firm.dto.FirmResponseV1;
import cargohub.orchestratorservice.infrastructure.client.hub.HubClient;
import cargohub.orchestratorservice.infrastructure.client.hub.dto.HubResponseV1;
import cargohub.orchestratorservice.infrastructure.client.order.OrderClient;
import cargohub.orchestratorservice.infrastructure.client.order.dto.CreateOrderClientRequestV1;
import cargohub.orchestratorservice.infrastructure.client.order.dto.CreateOrderClientRequestV1.OrderProductInfo;
import cargohub.orchestratorservice.infrastructure.client.order.dto.CreateOrderResponseV1;
import cargohub.orchestratorservice.infrastructure.client.product.ProductClient;
import cargohub.orchestratorservice.infrastructure.client.product.dto.BulkProductQueryRequestV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.BulkProductQueryResponseV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.BulkProductQueryResponseV1.ProductInfo;
import cargohub.orchestratorservice.infrastructure.client.product.dto.UpdateProductStockRequestV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.UpdateProductStockRequestV1.StockUpdateItemRequest;
import cargohub.orchestratorservice.infrastructure.client.slack.SlackClient;
import cargohub.orchestratorservice.infrastructure.client.slack.dto.request.SlackDeadlineRequestV1;
import cargohub.orchestratorservice.infrastructure.client.user.UserClient;
import cargohub.orchestratorservice.infrastructure.client.user.dto.response.UserResponseV1;
import cargohub.orchestratorservice.infrastructure.dto.CreateOrderRequestV1;
import cargohub.orchestratorservice.libs.error.BusinessException;
import cargohub.orchestratorservice.libs.error.ErrorCode;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrchestratorOrderService {

    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final FirmClient firmClient;
    private final HubClient hubClient;
    private final AiClient aiClient;
    private final UserClient userClient;
    private final SlackClient slackClient;

    public void createOrder(CreateOrderRequestV1 createOrderRequestV1) throws BusinessException {

        // 상품 조회
        BulkProductQueryRequestV1 requestV1 = new BulkProductQueryRequestV1(
            createOrderRequestV1.products().stream()
                .map(CreateOrderRequestV1.OrderProductRequest::productId)
                .toList());

        BulkProductQueryResponseV1 productMap = productClient.getProducts(requestV1);

        // 같은 공급 업체인지
        Set<UUID> supplierIds = productMap.products().values().stream()
            .map(ProductInfo::id)
            .collect(Collectors.toSet());

        if(supplierIds.size() > 1) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_SUPPLIER);
        }

        // 재고 차감
        List<StockUpdateItemRequest> stockUpdateItems = createOrderRequestV1.products().stream()
            .map(p -> new UpdateProductStockRequestV1.StockUpdateItemRequest(
                p.productId(),
                p.quantity()
            ))
            .toList();
        productClient.decreaseStock(new UpdateProductStockRequestV1(stockUpdateItems));

        // 주문 생성
        UUID supplierId = supplierIds.iterator().next();
        UUID receiverId = createOrderRequestV1.receiverId();

        List<OrderProductInfo> orderProducts = createOrderRequestV1.products().stream()
            .map(product -> {
                ProductInfo productInfo = productMap.products().get(product.productId());
                if(productInfo == null) {
                    throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
                }
                return new OrderProductInfo(
                    productInfo.id(),
                    productInfo.name(),
                    productInfo.stockQuantity(),
                    productInfo.price()
                );
            }).toList();

        // 단일 상품 정보
        OrderProductInfo orderProductInfo = orderProducts.get(0);

        CreateOrderClientRequestV1 orderRequest = new CreateOrderClientRequestV1(
            supplierId,
            receiverId,
            createOrderRequestV1.requestNote(),
            orderProducts,
            UUID.randomUUID()
        );

        CreateOrderResponseV1 order = orderClient.createOrder(orderRequest);

        // 공급 업체
        FirmResponseV1 supplier = firmClient.getFirm(supplierId);

        // 허브 정보 (출발지 / 경유지)
        HubResponseV1 supplierHub = hubClient.getHub(supplier.hubId());

        UserResponseV1 user = userClient.getUser(supplierHub.hubManagerId());

        // 수령업체 (도착지)
        FirmResponseV1 receiver = firmClient.getFirm(receiverId);
        HubResponseV1 receiverHub = hubClient.getHub(receiver.hubId());

        // 최종 발송 시한 계산 요청 구성
        CalculateAiDeadlineRequestV1 aiDeadlineRequest = CalculateAiDeadlineRequestV1.builder()
            .orderNum(order.id())
            .orderAt(order.createdAt())                         // 주문시간
            .productName(orderProductInfo.name())               // 상품 이름
            .quantity(orderProductInfo.quantity())              // 주문한 상품 수량
            .requestNote(order.requestNote())                   // 배송 요청 사항
            .shipFromHub(supplierHub.address().fullAddress())   // 출발 허브 주소
//            .viaHubs(delivery.)                               // 허브 경유지 주소 (없으면 "없음"으로 노출되는 로직 내부에 있음)
            .destination(receiver.address().fullAddress())      // 도착지 주소
            .build();

        // 최종 발송 시한 계산 응답
        AiDeadlineResponseV1 aiResponse = aiClient.calculateDeadlineWithPromptData(aiDeadlineRequest);

        // 슬랙 메시지 content 구성
        SlackDeadlineRequestV1 request = SlackDeadlineRequestV1.builder()
            .receiverSlackId(user.slackId())
            .orderInfo(aiResponse.orderInfo())
            .finalDeadline(aiResponse.finalDeadline())
            .aiLogId(aiResponse.aiLogId())
            .build();

        // 슬랙 메시지 전송
        slackClient.sendDeadlineNotice(request);

    }
}
