package ck4.nvb.rsmanagement.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String contact;

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    private Date createAt;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private Date updateAt;

    private Long createBy;

    private Long updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
