package com.cargohub.product_service.domain.entity;

import com.cargohub.product_service.domain.vo.CustomerId;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
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
    private Integer price;

    @Column(name = "sellable", nullable = false, columnDefinition = "tinyint")
    @Comment("상품 판매 가능 여부")
    private Boolean sellable;

    private LocalDateTime createdAt;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "created_by", updatable = false, nullable = false))
    private CustomerId createdBy;

    private LocalDateTime updatedAt;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "updated_by"))
    private CustomerId updatedBy;

    private LocalDateTime deletedAt;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "deleted_by"))
    private CustomerId deletedBy;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    private Product(String name, FirmId firmId, HubId hubId, Integer stockQuantity, Integer price, Boolean sellable, CustomerId createdBy) {
        this.name = name;
        this.firmId = firmId;
        this.hubId = hubId;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.sellable = sellable;
        this.createdBy = createdBy;
    }

    public static Product ofNewProduct(String name, FirmId firmId, HubId hubId, Integer stockQuantity, Integer price, Boolean sellable, CustomerId createdBy) {
        return new Product(name, firmId, hubId, stockQuantity, price, sellable, createdBy);
    }

    public void update(String name, Integer stockQuantity, Integer price, Boolean sellable, CustomerId updatedBy) {
        if(name != null) this.name = name;
        if(stockQuantity != null) this.stockQuantity = stockQuantity;
        if(price != null) this.price = price;
        if(sellable != null) this.sellable = sellable;

        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void delete(CustomerId deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }


}
