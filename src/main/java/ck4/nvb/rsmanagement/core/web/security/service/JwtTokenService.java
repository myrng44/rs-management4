package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRefreshRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;

public interface JwtTokenService {
    
    /**
     * Authenticate user and generate access token with refresh token
     */
    TokenResponseDto getToken(TokenRequestDto request) throws AppException;

    /**
     * Refresh access token using refresh token
     */
    TokenResponseDto refreshToken(TokenRefreshRequestDto request) throws AppException;

    /**
     * Remove refresh token (logout)
     */
    void removeToken(TokenRefreshRequestDto request) throws AppException;

    /**
     * Verify and extract user information from JWT token
     */
    UserRoleDto verifyToken(String token) throws AppException;
}
