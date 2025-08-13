package ck4.nvb.rsmanagement.core.module.order.saleallocation.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sale_allocation")
public class SaleAllocation extends FullAuditedSerialIdEntity {

    @Column(name = "sale_line_id", nullable = false)
    private Long saleLineId;

    @Column(name = "batch_stock_id", nullable = false)
    private Long batchStockId;

    @Column(name = "qty_allocated", nullable = false)
    private Integer qtyAllocated;

    @Column(name = "qty_picked", nullable = false)
    private Integer qtyPicked = 0;

    @Column(name = "unit_cost_snap", nullable = false)
    private Integer unitCostSnap;
}
