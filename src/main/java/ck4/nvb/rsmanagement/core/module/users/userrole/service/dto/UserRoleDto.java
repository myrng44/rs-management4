package ck4.nvb.rsmanagement.core.module.users.userrole.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserRoleDto extends EntityDto<Long> {
    // Core relationships
    private Long userId;
    private Long roleId;
    private Long storeId;
    
    // User information
    private String username;
    private String fullName;
    private String email;
    private String phone;
    
    // Role information
    private String roleName;
    private String roleDescription;
    
    // Permissions for this specific role
    private List<String> permissions;
    
    // Store information (optional)
    private String storeName;
}
