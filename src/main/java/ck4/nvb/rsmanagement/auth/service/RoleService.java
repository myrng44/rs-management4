package ck4.nvb.rsmanagement.auth.service;

import ck4.nvb.rsmanagement.auth.entity.Role;
import ck4.nvb.rsmanagement.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
