package ck4.nvb.rsmanagement.core.module.users.user.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.UserSessionDto;
import java.util.List;

public interface UserGetServiceWithRole {

  /** Authenticate user and return primary role (first role found) */
  UserRoleDto getByUsernameAndPassword(String username, String password) throws AppException;

  /** Authenticate user and return specific role for store */
  UserRoleDto getByUsernameAndPasswordAndStore(String username, String password, Long storeId)
      throws AppException;

  /** Get user by ID with primary role */
  UserRoleDto get(Long userId) throws AppException;

  /** Get user by username with primary role */
  UserRoleDto getByUsername(String username) throws AppException;

  /** Get all roles for a user across all stores */
  List<UserRoleDto> getAllUserRoles(Long userId) throws AppException;

  /** Get user session with all roles and current context */
  UserSessionDto getUserSession(Long userId, Long storeId) throws AppException;
}
