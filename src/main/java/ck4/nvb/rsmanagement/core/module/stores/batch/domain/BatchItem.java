package ck4.nvb.rsmanagement.core.module.stores.batch.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batch_item")
public class BatchItem extends FullAuditedSerialIdEntity {

  @Column(name = "batch_id")
  private Long batchId;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "qty")
  private Integer qty;

  @Column(name = "import_price")
  private Integer importPrice;
}
