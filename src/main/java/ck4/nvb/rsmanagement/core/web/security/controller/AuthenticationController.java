package ck4.nvb.rsmanagement.core.web.security.controller;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.BaseUserDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserCreateDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.impl.UserCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.impl.UserRoleServiceImpl;
import ck4.nvb.rsmanagement.core.web.security.service.JwtTokenService;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRefreshRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.publicUrl}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtTokenService jwtTokenService;
    private final UserCrudServiceImpl userCrudService;
    private final UserRoleServiceImpl userRoleService;

    /**
     * login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody TokenRequestDto request) {
        try {
            log.info("Login attempt for user: {}", request.getUsername());
            TokenResponseDto response = jwtTokenService.getToken(request);
            log.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            log.warn("Login failed for user: {} - {}", request.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Login error for user: {}", request.getUsername(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    //@PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity<BaseUserDto> register(@Valid @RequestBody UserCreateDto request) {
        try {
            BaseUserDto user = getBaseUserDto();

            log.info("Register attempt for user: {}", request.getUserName());
            BaseUserDto response = userCrudService.create(request, user);
            log.info("Register successful for user: {}", request.getUserName());
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            log.warn("Register failed for user: {} - {}", request.getUserName(), e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Register error for user: {}", request.getUserName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private static BaseUserDto getBaseUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();
        BaseUserDto user = new BaseUserDto();
        user.setId(userRole.getUserId());
        user.setUserName(userRole.getUserName());
        user.setFullName(userRole.getFullName());
        user.setEmail(userRole.getEmail());
        user.setPhone(userRole.getPhone());
        user.setStoreId(userRole.getStoreId());
        return user;
    }

    /**
     * refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        try {
            log.info("Token refresh attempt");
            TokenResponseDto response = jwtTokenService.refreshToken(request);
            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            log.warn("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Token refresh error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody TokenRefreshRequestDto request) {
        try {
            log.info("Logout attempt");
            jwtTokenService.removeToken(request);
            log.info("Logout successful");
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            log.warn("Logout failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Logout error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * validate token endpoint (test)
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                // This will be handled by JwtAuthenticationFilter
                return ResponseEntity.ok("Token is valid");
            }
            return ResponseEntity.badRequest().body("Invalid authorization header");
        } catch (Exception e) {
            log.error("Token validation error", e);
            return ResponseEntity.badRequest().body("Token validation failed");
        }
    }

    /**
     * get current user info
     */
    @GetMapping("/me")
    public ResponseEntity<UserRoleDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal class: {}", authentication.getPrincipal().getClass().getName());
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();

        UserRoleDto response = userRoleService.getFullInfoByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());

        return ResponseEntity.ok(response);
    }
} 