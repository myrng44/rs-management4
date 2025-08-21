package ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.repository;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.entity.RolePermission;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("rolePermissionRepository")
public interface RolePermissionRepository
    extends BaseFullAuditedRepository<RolePermission, Long, Long> {
  List<RolePermission> findByRoleId(Long roleId);
}
