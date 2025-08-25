package ck4.nvb.rsmanagement.core.module.users.permission.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "permission")
public class Permission extends FullAuditedSerialIdEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "code", nullable = false, unique = true)
  private PermissionCode code;

  @Column(name = "\"desc\"")
  private String desc;
}
