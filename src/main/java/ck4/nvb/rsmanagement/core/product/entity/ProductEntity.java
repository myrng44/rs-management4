package ck4.nvb.rsmanagement.core.product.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProductEntity extends FullAuditedSerialIdEntity {
    @Column(name = "name", columnDefinition = "NVARCHAR(100)")
    private String name;

    @Column(name = "sku")
    private String sku;

    @Column(name = "description", columnDefinition = "NVARCHAR(250)")
    private String description;

    @Column(name = "unit_price")
    private int unitPrice;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "supplier_id")
    private Long supplierId;
}
