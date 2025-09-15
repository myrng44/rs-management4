package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_allocation")
public class SaleAllocation extends FullAuditedSerialIdEntity {
  @Column(name = "sale_line_id")
  private Long saleLineId;

  @Column(name = "batch_item_id")
  private Long batchItemId;

  @Column(name = "sold_qty")
  private Integer soldQty;

  @Column(name = "unit_cost_snap")
  private Integer unitCostSnap;
}
