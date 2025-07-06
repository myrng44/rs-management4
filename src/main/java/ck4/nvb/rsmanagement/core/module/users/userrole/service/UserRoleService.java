package ck4.nvb.rsmanagement.core.module.users.userrole.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;

public interface UserRoleService extends FullAuditedCrudService<UserRoleDto, UserRole, Long, UserGetDto, Long> {
}
