package ck4.nvb.rsmanagement.core.module.users.permission.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "permission")
public class Permission extends FullAuditedSerialIdEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
}
