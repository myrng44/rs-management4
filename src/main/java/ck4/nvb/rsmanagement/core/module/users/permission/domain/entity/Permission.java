package ck4.nvb.rsmanagement.core.module.users.permission.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.entity.RolePermission;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Permission extends FullAuditedSerialIdEntity {

  @Column(name = "code")
  private String code;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<RolePermission> rolePermissions;
}
