package ck4.nvb.rsmanagement.core.module.order.voucher.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher extends FullAuditedSerialIdEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "discount_value")
    private Integer discountValue;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
}
