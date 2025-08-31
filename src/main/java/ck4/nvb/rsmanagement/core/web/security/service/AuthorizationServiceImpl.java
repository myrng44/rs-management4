package ck4.nvb.rsmanagement.core.web.security.service;

import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.RoleName;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserGetServiceWithRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("authorizationService")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
  private final UserGetServiceWithRole userGetServiceWithRole;

  public boolean hasPermission(PermissionCode permission) {
    try {
      UserRoleDto currentUser = getCurrentUser();
      return currentUser.getPermissions().contains(permission.getCode());
    } catch (Exception e) {
      log.warn("Permission check failed", e);
      return false;
    }
  }

  public boolean hasPermissionAtStore(PermissionCode permission, Long storeId) {
    try {
      UserRoleDto currentUser = getCurrentUser();

      // If checking current store, use cached permissions
      if (storeId.equals(currentUser.getStoreId())) {
        return currentUser.getPermissions().contains(permission.getCode());
      }

      // For different store, get fresh data
      return userGetServiceWithRole.hasPermissionAtStore(
          currentUser.getUserId(), storeId, permission);
    } catch (Exception e) {
      log.warn("Permission check failed for store: {}", storeId, e);
      return false;
    }
  }

  public boolean hasAccessToStore(Long storeId) {
    try {
      UserRoleDto currentUser = getCurrentUser();
      return userGetServiceWithRole.hasAccessToStore(currentUser.getUserId(), storeId);
    } catch (Exception e) {
      log.warn("Store access check failed for store: {}", storeId, e);
      return false;
    }
  }

  public void requirePermission(PermissionCode permission) {
    if (!hasPermission(permission)) {
      throw new SecurityException("Access denied: " + permission.getCode());
    }
  }

  public void requirePermissionAtStore(PermissionCode permission, Long storeId) {
    if (!hasPermissionAtStore(permission, storeId)) {
      throw new AccessDeniedException(
          "Access denied: " + permission.getCode() + " at store " + storeId);
    }
  }

  public UserRoleDto getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof UserRoleDto)) {
      throw new SecurityException("User not authenticated");
    }
    return (UserRoleDto) auth.getPrincipal();
  }

  public Long getCurrentStoreId() {
    return getCurrentUser().getStoreId();
  }

  public RoleName getCurrentRole() {
    String roleName = getCurrentUser().getRoleName();
    return RoleName.fromName(roleName);
  }
}
