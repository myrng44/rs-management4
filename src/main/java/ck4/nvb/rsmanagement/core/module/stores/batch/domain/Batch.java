package ck4.nvb.rsmanagement.core.module.stores.batch.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batch")
public class Batch extends FullAuditedSerialIdEntity {

  @Column(name = "batch_id")
  private Long batchId;

  @Column(name = "batch_code")
  private String batchCode;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "supplier_id")
  private Long supplierId;

  @Column(name = "ogirinal_quantity")
  private Integer quantity;

  @Column(name = "imported_price")
  private Integer importedPrice;

  @Column(name = "manufacturing_date")
  private Date manufacturingDate;

  @Column(name = "expiry_date")
  private Date expiryDate;

  @Column(name = "arrival_date")
  private LocalDate arrivalDate;
}
