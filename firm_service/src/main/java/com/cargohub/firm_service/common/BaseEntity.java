package com.cargohub.firm_service.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    protected UUID createdBy;

    @LastModifiedBy
    protected UUID updatedBy;

    protected UUID deletedBy;

    protected LocalDateTime deletedAt;

    public void delete(UUID userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void recover() {
        this.deletedBy = null;
        this.deletedAt = null;
    }
}
