package ck4.nvb.rsmanagement.core.module.stores.store.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.Enable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "store")
public class Store extends FullAuditedSerialIdEntity implements Enable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Override
    public boolean getEnable() {
        return enabled;
    }

    @Override
    public void setEnable(Boolean active) {
        this.enabled = active;
    }
}
