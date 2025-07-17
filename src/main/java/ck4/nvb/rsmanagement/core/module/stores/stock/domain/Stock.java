package ck4.nvb.rsmanagement.core.module.stores.stock.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.Enable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock extends FullAuditedSerialIdEntity implements Enable {

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "enabled")
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
