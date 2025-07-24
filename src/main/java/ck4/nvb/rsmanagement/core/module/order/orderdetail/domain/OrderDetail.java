package ck4.nvb.rsmanagement.core.module.order.orderdetail.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail extends FullAuditedSerialIdEntity {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;
}
