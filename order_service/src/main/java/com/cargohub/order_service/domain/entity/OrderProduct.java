package com.cargohub.order_service.domain.entity;

import com.cargohub.order_service.domain.vo.ProductId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "p_order_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "product_id", nullable = false))
    private ProductId productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
    }

    public OrderProduct(ProductId productId, String productName, Integer productPrice, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public static OrderProduct ofNewOrderProduct(ProductId productId, String productName, Integer productPrice, Integer quantity) {
        return new OrderProduct(productId, productName, productPrice, quantity);
    }

}
