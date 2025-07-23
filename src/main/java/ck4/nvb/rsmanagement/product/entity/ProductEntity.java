package ck4.nvb.rsmanagement.product.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class) // cho phép tự động add time
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal unitPrice;

    private Long categoryId;

    private Long supplierId;

    @CreatedDate // tự động thêm thời gian tạo
//    @Temporal(TemporalType.TIMESTAMP) // tạo thời gian đúng nhất
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    private Long createdBy;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private Date lastUpdatedAt;

    private Long updateBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
