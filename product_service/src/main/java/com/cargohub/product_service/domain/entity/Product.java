package com.cargohub.product_service.domain.entity;

import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "p_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="name", length = 100, nullable = false)
    @Comment("상품 명")
    private String name;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "firm_id", nullable = false))
    @Comment("업체 ID")
    private FirmId firmId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false))
    @Comment("상품관리 허브 ID")
    private HubId hubId;

    @Column(name = "stock_quantity", nullable = true)
    @Comment("재고수량")
    private Integer stockQuantity;

    @Column(name = "price", nullable = false)
    @Comment("가격")
    private BigDecimal price;

    @Column(name = "sellable", nullable = false, columnDefinition = "tinyint")
    @Comment("상품 판매 가능 여부")
    private Boolean sellable;

    private Product(String name, FirmId firmId, HubId hubId, Integer stockQuantity, BigDecimal price, Boolean sellable, UUID createdBy) {
        this.name = name;
        this.firmId = firmId;
        this.hubId = hubId;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.sellable = sellable;
        this.createdBy = createdBy;
    }

    public static Product ofNewProduct(String name, FirmId firmId, HubId hubId, Integer stockQuantity, BigDecimal price, Boolean sellable, UUID createdBy) {
        return new Product(name, firmId, hubId, stockQuantity, price, sellable, createdBy);
    }

    public void update(String name, Integer stockQuantity, BigDecimal price, Boolean sellable, UUID updatedBy) {
        if(name != null) this.name = name;
        if(stockQuantity != null) this.stockQuantity = stockQuantity;
        if(price != null) this.price = price;
        if(sellable != null) this.sellable = sellable;

        this.updatedBy = updatedBy;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
