package ck4.nvb.rsmanagement.core.module.order.voucher.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher extends FullAuditedSerialIdEntity {

  @Column(name = "code")
  private String code;

  @Column(name = "desc")
  private String desc;

  @Column(name = "discount_percent")
  private Integer discountPercent;

  @Column(name = "discount_value")
  private Integer discountValue;

  @Column(name = "valid_from")
  private LocalDateTime validFrom;

  @Column(name = "valid_to")
  private LocalDateTime validTo;

  @Column(name = "qty_total")
  private Long qtyTotal;

  @Column(name = "qty_redeemed")
  private Long qtyRedeemed;

  @Column(name = "per_customer_limit")
  private Integer perCustomerLimit;

  @Column(name = "audience_type")
  private String audienceType;
}
