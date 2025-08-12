package ck4.nvb.rsmanagement.core.auth.controller;

import ck4.nvb.rsmanagement.core.auth.entity.Role;
import ck4.nvb.rsmanagement.core.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}
