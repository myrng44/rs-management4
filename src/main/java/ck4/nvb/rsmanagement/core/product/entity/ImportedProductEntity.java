package ck4.nvb.rsmanagement.core.product.entity;

import ck4.nvb.rsmanagement.base.domain.entity.CreationAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ImportedProductEntity extends CreationAuditedSerialIdEntity {
    @Column(name = "import_log_id")
    private String importLogId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "import_price")
    private int importPrice;
}
