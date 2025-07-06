package ck4.nvb.rsmanagement.core.web.security.controller;

import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    /**
     * Public endpoint - no authentication required
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a public endpoint");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * Protected endpoint - requires authentication
     */
    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a protected endpoint");
        response.put("user", userRole.getUsername());
        response.put("userId", userRole.getUserId());
        response.put("storeId", userRole.getStoreId());
        response.put("roleId", userRole.getRoleId());
        response.put("permissions", userRole.getPermissions());
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("User {} accessed protected endpoint", userRole.getUsername());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Admin only endpoint - requires USER_WRITE permission
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<Map<String, Object>> adminEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is an admin-only endpoint");
        response.put("user", userRole.getUsername());
        response.put("permissions", userRole.getPermissions());
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("Admin user {} accessed admin endpoint", userRole.getUsername());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Store-specific endpoint - requires STORE_ACCESS permission
     */
    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasAuthority('STORE_ACCESS') and #storeId == authentication.principal.storeId")
    public ResponseEntity<Map<String, Object>> storeEndpoint(@PathVariable Long storeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a store-specific endpoint");
        response.put("user", userRole.getUsername());
        response.put("requestedStoreId", storeId);
        response.put("userStoreId", userRole.getStoreId());
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("User {} accessed store {} endpoint", userRole.getUsername(), storeId);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDto userRole = (UserRoleDto) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userRole.getUserId());
        response.put("username", userRole.getUsername());
        response.put("fullName", userRole.getFullName());
        response.put("email", userRole.getEmail());
        response.put("phone", userRole.getPhone());
        response.put("storeId", userRole.getStoreId());
        response.put("roleId", userRole.getRoleId());
        response.put("roleName", userRole.getRoleName());
        response.put("permissions", userRole.getPermissions());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
} 