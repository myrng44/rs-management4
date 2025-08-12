package ck4.nvb.rsmanagement.core.order.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedGeneratedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OderEntity extends FullAuditedGeneratedIdEntity {
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "final_price")
    private int finalPrice;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "note")
    private String note;
}
