//package com.cargohub.delivery_service.application.dto.request;
//
//import java.util.UUID;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor
//public class DeliveryRequestV1 {
//
//    private UUID orderId;
//
//    private UUID fromHubId;
//    private UUID toHubId;
//    private UUID destinationId;
//
//    private String receiverSlackId;
//
//    // 전체 필드 생성자 (직접 정의 — @AllArgsConstructor 금지됨)
//    public DeliveryRequestV1(
//        UUID orderId,
//        UUID fromHubId,
//        String fromHubAddress,
//        UUID toHubId,
//        String toHubAddress,
//        UUID destinationId,
//        String destinationAddress,
//        String requestNote,
//        String receiverSlackId
//    ) {
//        this.orderId = orderId;
//        this.fromHubId = fromHubId;
//        this.fromHubAddress = fromHubAddress;
//        this.toHubId = toHubId;
//        this.toHubAddress = toHubAddress;
//        this.destinationId = destinationId;
//        this.destinationAddress = destinationAddress;
//        this.requestNote = requestNote;
//        this.receiverSlackId = receiverSlackId;
//    }
//}
