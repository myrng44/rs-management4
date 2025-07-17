package ck4.nvb.rsmanagement.core.module.users.userrole.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserRoleDto extends EntityDto<Long> {
    //main relationships
    private Long userId;
    private Long roleId;
    private Long storeId;
    
    //user information (optional)
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    
    //role information
    private String roleName;
    
    //permissions for role
    private List<String> permissions;
}
