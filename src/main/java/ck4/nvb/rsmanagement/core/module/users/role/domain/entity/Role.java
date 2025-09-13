package ck4.nvb.rsmanagement.core.module.users.role.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role extends FullAuditedSerialIdEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, unique = true)
  private RoleName name;

  @Column(name = "description")
  private String desc;
}
