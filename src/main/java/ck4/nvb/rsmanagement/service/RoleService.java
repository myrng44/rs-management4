package ck4.nvb.rsmanagement.service;

import ck4.nvb.rsmanagement.entity.Role;
import ck4.nvb.rsmanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public Role findByName(String roleName) {
        return roleRepository.findRoleByName(roleName);
    }
}
