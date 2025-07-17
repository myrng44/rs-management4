package ck4.nvb.rsmanagement.core.module.stores.supplier.domain;


import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.Enable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "supplier")
public class Supplier extends FullAuditedSerialIdEntity implements Enable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

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
