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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @CreatedDate
    @Column(name = "craeted_at", nullable = false, updatable = false)
    private Date createdAt;

    private Long createdBy;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private Date updateAt;

    private Long updatedBy;

    private boolean is_deleted;
}
