package ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain;

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
@Table(name = "batch_stock")
public class BatchStock extends FullAuditedSerialIdEntity {
  @Column(name = "batch_id")
  private Long batchId;

  @Column(name = "store_id")
  private Long storeId;

  @Column(name = "status")
  private String status;

  @Column(name = "version")
  private Integer version;
}
