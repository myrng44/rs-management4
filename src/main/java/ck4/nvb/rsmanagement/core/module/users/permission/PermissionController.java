package ck4.nvb.rsmanagement.core.module.users.permission;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.Permission;
import ck4.nvb.rsmanagement.core.module.users.permission.service.IPermissionService;
import ck4.nvb.rsmanagement.core.module.users.permission.service.dto.PermissionDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/permissions")
public class PermissionController extends AuditedCrudController<PermissionDto, Permission, Long, UserGetDto, Long, PermissionDto, PermissionDto> {

    public PermissionController(IPermissionService service) {
        super(service);
    }

    @Override
    public UserGetDto extractUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserGetDto) {
            return (UserGetDto) principal;
        }

        if (principal instanceof UserRoleDto) {
            UserRoleDto userRoleDto = (UserRoleDto) principal;
            UserGetDto userGetDto = new UserGetDto();
            userGetDto.setId(userRoleDto.getUserId());
            userGetDto.setUserName(userRoleDto.getUserName());
            return userGetDto;
        }
        return null;
    }
}
