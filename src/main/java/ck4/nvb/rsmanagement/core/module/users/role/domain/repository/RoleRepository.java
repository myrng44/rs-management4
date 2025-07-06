package ck4.nvb.rsmanagement.core.module.users.role.domain.repository;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.Role;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends BaseFullAuditedRepository<Role, Long, Long> {
    Role findByName(String name);
}
