package ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role_permission")
public class RolePermission extends FullAuditedSerialIdEntity {

  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "permission_id")
  private Long permissionId;
}
