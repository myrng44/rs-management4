package ck4.nvb.rsmanagement.core.web.security.controller;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.web.security.service.JwtTokenService;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRefreshRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenRequestDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtTokenService jwtTokenService;

    /**
     * Login endpoint
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

    /**
     * Refresh token endpoint
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
     * Logout endpoint
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
     * Validate token endpoint (for testing)
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
} 