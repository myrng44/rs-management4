package ck4.nvb.rsmanagement.core.module.users.role.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {
  SYSADMIN("Quản trị hệ thống"),
  ADMIN("Quản trị viên"),
  MANAGER("Quản lý"),
  STAFF("Nhân viên"),
  INVENTORY_STAFF("INVENTORY_STAFF");

  private final String name;

  public static RoleName fromName(String name) {
    for (RoleName role : values()) {
      if (role.name.equals(name)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Unknown role name: " + name);
  }
}
