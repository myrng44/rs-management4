package ck4.nvb.rsmanagement.core.module.order.order.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedGeneratedIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.GeneratedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends FullAuditedGeneratedIdEntity {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "final_price")
    private int finalPrice;

    @Column(name = "note")
    private String note;

    @Column(name = "payment_id")
    private Long paymentId;
}
