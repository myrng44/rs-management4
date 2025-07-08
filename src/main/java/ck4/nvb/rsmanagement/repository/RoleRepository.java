package ck4.nvb.rsmanagement.repository;

import ck4.nvb.rsmanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
