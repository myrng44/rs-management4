package ck4.nvb.rsmanagement.core.order.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodEntity extends FullAuditedSerialIdEntity {
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
