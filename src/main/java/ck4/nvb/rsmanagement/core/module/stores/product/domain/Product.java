package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.Enable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product extends FullAuditedSerialIdEntity implements Enable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Override
    public boolean getEnable() {
        return enabled;
    }

    @Override
    public void setEnable(Boolean active) {
        this.enabled = active;
    }
}
