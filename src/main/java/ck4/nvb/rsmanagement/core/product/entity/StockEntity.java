package ck4.nvb.rsmanagement.core.product.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity extends FullAuditedSerialIdEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;
}
