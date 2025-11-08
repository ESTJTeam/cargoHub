package com.cargohub.order_service.domain.entity;

import com.cargohub.order_service.common.BaseEntity;
import com.cargohub.order_service.domain.exception.OrderErrorCode;
import com.cargohub.order_service.domain.exception.OrderException;
import com.cargohub.order_service.domain.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    List<OrderProduct> orderProducts = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "supplier_id", nullable = false))
    @Comment("공급 업체 ID")
    private SupplierId supplierId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "receiver_id", nullable = false))
    @Comment("수령 업체 ID")
    private ReceiverId receiverId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_delivery_id"))
    @Comment("허브 배송 ID")
    private HubDeliveryId hubDeliveryId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "firm_delivery_id"))
    @Comment("업체 배송 ID")
    private FirmDeliveryId firmDeliveryId;

    @Column(name = "request_note", nullable = true, columnDefinition = "text")
    @Comment("요구사항")
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Comment("주문상태")
    private OrderStatus status;


    private Order(UUID createdBy, List<OrderProduct> orderProducts, SupplierId supplierId, ReceiverId receiverId, HubDeliveryId hubDeliveryId, FirmDeliveryId firmDeliveryId, String requestNote, OrderStatus status) {
        this.createdBy = createdBy;
        this.orderProducts = orderProducts;
        this.supplierId = supplierId;
        this.receiverId = receiverId;
        this.hubDeliveryId = hubDeliveryId;
        this.firmDeliveryId = firmDeliveryId;
        this.requestNote = requestNote;
        this.status = status;
    }

    public static Order ofNewOrder(UUID createdBy, List<OrderProduct> orderProducts, SupplierId supplierId, ReceiverId receiverId, String requestNote) {

        validateOrderProducts(orderProducts);

        return new Order(createdBy, orderProducts, supplierId, receiverId,null, null, requestNote, OrderStatus.PREPARING);
    }

    private static void validateOrderProducts(List<OrderProduct> orderProducts) {

        if(orderProducts.isEmpty()) {
            throw new OrderException(OrderErrorCode.ORDER_PRODUCT_EMPTY);
        }
    }

    public void ship(HubDeliveryId hubDeliveryId, FirmDeliveryId firmDeliveryId) {
        this.hubDeliveryId = hubDeliveryId;
        this.firmDeliveryId = firmDeliveryId;
        this.status = OrderStatus.SHIPPED;
    }

    public void updateStatus(OrderStatus status, UUID updatedBy){
        this.status = status;
        this.updatedBy = updatedBy;
    }

    public void cancel(UUID deletedBy) {

        if(!status.canBeCancelled()) {
            throw new OrderException(OrderErrorCode.ORDER_CANCELLATION_NOT_ALLOWED);
        }

        this.status = OrderStatus.CANCELLED;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
