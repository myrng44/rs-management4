package ck4.nvb.rsmanagement.core.auth.repository;

import ck4.nvb.rsmanagement.core.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
