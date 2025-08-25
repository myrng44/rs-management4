package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.RoleName;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;

public interface AuthorizationService {

    /**
     * Check if current user has permission at their current store
     */
    public boolean hasPermission(PermissionCode permission);

    /**
     * Check if current user has permission at specific store
     */
    public boolean hasPermissionAtStore(PermissionCode permissionCode, Long storeId);

    /**
     * Check if current user has access to store
     */
    public boolean hasAccessToStore(Long storeId);


    /**
     * Require permission or throw exception
     */
    public void requirePermission(PermissionCode permission);

    /**
     * Require permission at store or throw exception
     */
    public void requirePermissionAtStore(PermissionCode permission, Long storeId);

    /**
     * Get current authenticated user
     */
    public UserRoleDto getCurrentUser();

    /**
     * Get current user's store ID
     */
    public Long getCurrentStoreId();

    /**
     * Get current user's role
     */
    public RoleName getCurrentRole();
}
