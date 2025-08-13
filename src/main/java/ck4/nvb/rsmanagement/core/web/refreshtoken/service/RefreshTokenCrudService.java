package ck4.nvb.rsmanagement.core.web.refreshtoken.service;

import ck4.nvb.rsmanagement.base.application.service.CreationAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshToken;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenDto;

public interface RefreshTokenCrudService extends CreationAuditedCrudService<RefreshTokenDto, RefreshToken, String, UserGetDto, Long> {

    void deleteAll(String deviceSession, Long creatorId);
}
