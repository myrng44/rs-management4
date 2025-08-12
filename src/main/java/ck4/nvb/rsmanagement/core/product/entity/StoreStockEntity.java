package ck4.nvb.rsmanagement.core.product.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreStockEntity extends FullAuditedSerialIdEntity {
    private Long productId;
    private Long storeId;
    private int quantity;
    private int minQuantity;
}
