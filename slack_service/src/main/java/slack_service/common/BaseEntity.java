package slack_service.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    protected UUID createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    protected UUID updatedBy;

    protected LocalDateTime deletedAt;

    protected UUID deletedBy;

    // TODO - User 정보 가져와서 deletedBy 추가
    public void delete() {
//        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    // TODO - User 정보 가져와서 updatedBy 추가
    public void recover() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = LocalDateTime.now();
//        this.updatedBy = updatedBy;
    }

    public boolean checkDeleted() {
        return deletedAt != null || deletedBy != null;
    }
}
