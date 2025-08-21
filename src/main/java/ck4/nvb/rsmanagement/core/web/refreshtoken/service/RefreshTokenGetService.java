package ck4.nvb.rsmanagement.core.web.refreshtoken.service;

import ck4.nvb.rsmanagement.base.application.service.GetService;
import ck4.nvb.rsmanagement.core.web.refreshtoken.domain.RefreshToken;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenGetDto;

public interface RefreshTokenGetService
    extends GetService<RefreshTokenGetDto, RefreshToken, String> {}
