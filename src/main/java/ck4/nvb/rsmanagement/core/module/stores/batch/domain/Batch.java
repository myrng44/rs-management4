package ck4.nvb.rsmanagement.core.module.stores.batch.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
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

  @Column(name = "batch_code")
  private String batchCode;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "original_qty")
  private Integer originalQty;

  @Column(name = "supplier_id")
  private Long supplierId;

  @Column(name = "imported_price")
  private Integer importedPrice;

  @Column(name = "manufacture_date")
  private LocalDateTime manufactureDate;

  @Column(name = "expiry_date")
  private LocalDateTime expiryDate;

  @Column(name = "arrival_date")
  private LocalDateTime arrivalDate;
}
