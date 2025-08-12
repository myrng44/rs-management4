package ck4.nvb.rsmanagement.core.order.entity;

import ck4.nvb.rsmanagement.base.domain.entity.CreationAuditedGeneratedIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.CreationAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEntity extends CreationAuditedSerialIdEntity {
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;
}
