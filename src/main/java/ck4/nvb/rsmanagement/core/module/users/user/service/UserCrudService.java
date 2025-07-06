package ck4.nvb.rsmanagement.core.module.users.user.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.users.user.domain.entity.User;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.BaseUserDto;

public interface UserCrudService extends FullAuditedCrudService<BaseUserDto, User, Long, BaseUserDto, Long> {
}
