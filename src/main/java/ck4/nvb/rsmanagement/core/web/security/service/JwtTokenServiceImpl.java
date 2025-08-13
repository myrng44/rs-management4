package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserGetServiceWithRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.RefreshTokenCrudService;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.RefreshTokenGetService;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenDto;
import ck4.nvb.rsmanagement.core.web.refreshtoken.service.dto.RefreshTokenGetDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRefreshRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;
import ck4.nvb.rsmanagement.core.web.security.service.rsa.RSAKeyProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service("jwtTokenService")
@Transactional
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rs.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final UserGetServiceWithRole userService;
    private final RefreshTokenCrudService refreshTokenService;
    private final RefreshTokenGetService refreshTokenGetService;
    private final JwtTokenGenerator tokenGenerator;
    private final RSAKeyProperties rsaKeys;

    @Override
    public TokenResponseDto getToken(TokenRequestDto request) throws AppException {
        //authenticate user
        UserRoleDto userRole;
        if (request.getStoreId() != null) {
            //login to a store
            userRole = userService.getByUsernameAndPasswordAndStore(
                request.getUsername(), 
                request.getPassword(),
                request.getStoreId()
            );
        } else {
            //login with primary role
            userRole = userService.getByUsernameAndPassword(
                request.getUsername(), 
                request.getPassword()
            );
        }

        logger.info("[traceId-{}] User {}|{} - Get Access Token from IP: {} for Store: {}", 
            request.getTraceId(), userRole.getUserId(), userRole.getUserName(),
            request.getIpAddress(), userRole.getStoreId());

        //generate access token
        TokenResponseDto response = tokenGenerator.generateToken(userRole, rsaKeys.getPrivateKey());

        //generate refresh token
        String deviceSession = request.getDeviceSession();
        if (deviceSession == null) {
            deviceSession = request.getIpAddress() + "-" + userRole.getUserId();
        }

        //delete previous refresh tokens for this device session
        if (deviceSession != null) {
            refreshTokenService.deleteAll(deviceSession, userRole.getUserId());
        }

        //create new refresh token
        RefreshTokenDto refreshToken = new RefreshTokenDto();
        refreshToken.setId(UUID.randomUUID().toString());
        refreshToken.setDeviceSession(deviceSession);
        refreshToken.setIpAddress(request.getIpAddress());
        refreshToken.setExpiredTime(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));

        //save refresh token
        refreshTokenService.create(refreshToken, createUserGetDto(userRole));
        response.setRefreshToken(refreshToken.getId());

        return response;
    }

    @Override
    public TokenResponseDto refreshToken(TokenRefreshRequestDto request) throws AppException {
        //get and validate refresh token
        RefreshTokenGetDto refreshToken = refreshTokenGetService.get(request.getRefreshToken());
        if (refreshToken == null) {
            logger.error("Refresh Token not found");
            throw new AppException("Refresh Token not found");
        }

        //check device session
        String deviceSession = request.getDeviceSession();
        if (deviceSession == null) {
            deviceSession = request.getIpAddress() + "-" + refreshToken.getCreatorId();
        }

        if (refreshToken.getDeviceSession() != null && 
            !refreshToken.getDeviceSession().equals(deviceSession)) {
            logger.error("Invalid refresh token - device session mismatch");
            throw new AppException("Invalid refresh token");
        }

        //check if refresh token is expired
        if (refreshToken.getExpiredTime().isBefore(LocalDateTime.now())) {
            refreshTokenService.delete(refreshToken.getId(), createUserGetDto(refreshToken.getCreatorId()));
            logger.error("Refresh Token expired");
            throw new AppException("Expired refresh token");
        }

        //get user role and generate new access token
        UserRoleDto userRole = userService.get(refreshToken.getCreatorId());
        TokenResponseDto response = tokenGenerator.generateToken(userRole, rsaKeys.getPrivateKey());
        response.setRefreshToken(refreshToken.getId());

        return response;
    }

    @Override
    public void removeToken(TokenRefreshRequestDto request) throws AppException {
        RefreshTokenGetDto refreshToken = refreshTokenGetService.get(request.getRefreshToken());
        if (refreshToken != null) {
            refreshTokenService.delete(refreshToken.getId(), createUserGetDto(refreshToken.getCreatorId()));
        }
    }

    @Override
    public UserRoleDto verifyToken(String token) throws AppException {
        return tokenGenerator.getUserDetailsFromToken(token, rsaKeys.getPublicKey());
    }

    private UserGetDto createUserGetDto(UserRoleDto userRole) {
        return new UserGetDto(userRole.getUserId());
    }

    private UserGetDto createUserGetDto(Long userId) {
        return new UserGetDto(userId);
    }
}
