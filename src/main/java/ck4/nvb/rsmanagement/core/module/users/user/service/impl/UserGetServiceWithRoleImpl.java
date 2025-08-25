package ck4.nvb.rsmanagement.core.module.users.user.service.impl;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.Permission;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.repository.PermissionRepository;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.Role;
import ck4.nvb.rsmanagement.core.module.users.role.domain.repository.RoleRepository;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.entity.RolePermission;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.repository.RolePermissionRepository;
import ck4.nvb.rsmanagement.core.module.users.user.domain.User;
import ck4.nvb.rsmanagement.core.module.users.user.domain.UserRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserGetServiceWithRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.repository.UserRoleRepository;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.UserSessionDto;
import ck4.nvb.rsmanagement.core.web.util.CommonPasswordEncoder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userGetServiceWithRole")
@RequiredArgsConstructor
@Slf4j
public class UserGetServiceWithRoleImpl implements UserGetServiceWithRole {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final RoleRepository roleRepository;
  private final RolePermissionRepository rolePermissionRepository;
  private final PermissionRepository permissionRepository;
  private final CommonPasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public UserRoleDto getByUsernameAndPassword(String username, String password)
      throws AppException {
    User user = authenticateUser(username, password);
    return getPrimaryUserRole(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserRoleDto getByUsernameAndPasswordAndStore(
      String username, String password, Long storeId) throws AppException {
    User user = authenticateUser(username, password);
    return getUserRoleForStore(user, storeId);
  }

  @Override
  @Transactional(readOnly = true)
  public UserRoleDto get(Long userId) throws AppException {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));
    return getPrimaryUserRole(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserRoleDto getByUsername(String username) throws AppException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException("User not found"));
    return getPrimaryUserRole(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserRoleDto> getAllUserRoles(Long userId) throws AppException {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));

    List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
    if (userRoles.isEmpty()) {
      throw new AppException("User has no roles assigned");
    }
    return userRoles.stream().map(this::buildUserRoleDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public UserSessionDto getUserSession(Long userId, Long storeId) throws AppException {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppException("User not found"));

    List<UserRoleDto> allRoles = getAllUserRoles(userId);
    UserRoleDto currentRole =
        allRoles.stream()
            .filter(role -> role.getStoreId().equals(storeId))
            .findFirst()
            .orElseThrow(() -> new AppException("User has no role for this store"));

    UserSessionDto session = new UserSessionDto(currentRole);
    session.setUserRoles(currentRole);
    return session;
  }

  private User authenticateUser(String username, String password) throws AppException {
    User user = findUserByUsername(username);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      log.warn("Authentication failed for user: {} - Invalid password", username);
      throw new AppException("Invalid credentials");
    }

    if (user.isDeleted()) {
      log.warn("Authentication failed for user: {} - Account is disabled", username);
      throw new AppException("Account is disabled");
    }
    log.info("User authenticated successfully: {}", username);
    return user;
  }

  /**
   * Lấy tất cả stores mà user có quyền truy cập
   */
  @Transactional(readOnly = true)
  public Set<Long> getUserAccessibleStores(Long userId) throws AppException {
    User user = findUserById(userId);
    return userRoleRepository.findStoreIdsByUserId(userId);
  }

  /**
   * Kiểm tra xem user có quyền truy cập store không
   */
  @Transactional(readOnly = true)
  public boolean hasAccessToStore(Long userId, Long storeId) throws AppException {
    return getUserAccessibleStores(userId).contains(storeId);
  }

  /**
   * Kiểm tra xem user có permission cụ thể ở store không
   */
  @Transactional(readOnly = true)
  public boolean hasPermissionAtStore(Long userId, Long storeId, PermissionCode permission)
          throws AppException {
    try {
      UserRoleDto userRole = getUserRoleForStore(findUserById(userId), storeId);
      return userRole.getPermissions().contains(permission.getCode());
    } catch (AppException e) {
      return false;
    }
  }

  private UserRoleDto getPrimaryUserRole(User user) throws AppException {
    List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
    if (userRoles.isEmpty()) {
      throw new AppException("User has no roles assigned");
    }

    // get first role (primary role)
    UserRole userRole = userRoles.get(0);
    return buildUserRoleDto(userRole, user);
  }

  private UserRoleDto getUserRoleForStore(User user, Long storeId) throws AppException {
    List<UserRole> userRoles = userRoleRepository.findByUserIdAndStoreId(user.getId(), storeId);
    if (userRoles.isEmpty()) {
      throw new AppException("User has no role for store ID: " + storeId);
    }

    // Một user chỉ có 1 role per store
    UserRole userRole = userRoles.get(0);
    return buildUserRoleDto(userRole, user);
  }

  private UserRoleDto findUserRoleForStore(List<UserRoleDto> roles, Long storeId)
          throws AppException {
    return roles.stream()
            .filter(role -> role.getStoreId().equals(storeId))
            .findFirst()
            .orElseThrow(() -> new AppException("User has no role for store ID: " + storeId));
  }

  private UserRoleDto buildUserRoleDto(UserRole userRole, User user) throws AppException {
    // Lấy thông tin role
    Role role = roleRepository.findById(userRole.getRoleId())
            .orElseThrow(() -> new AppException("Role not found with ID: " + userRole.getRoleId()));

    // Lấy permissions cho role
    List<PermissionCode> permissions = getPermissionsForRole(role.getId());

    UserRoleDto userRoleDto = new UserRoleDto();
    userRoleDto.setId(userRole.getId());
    userRoleDto.setUserId(user.getId());
    userRoleDto.setRoleId(role.getId());
    userRoleDto.setStoreId(userRole.getStoreId());

    // user information
    userRoleDto.setUserName(user.getUsername());
    userRoleDto.setFullName(user.getName());
    userRoleDto.setEmail(user.getEmail());
    userRoleDto.setPhone(user.getPhone());

    // role information
    userRoleDto.setRoleName(role.getName().name());

    // permissions
    userRoleDto.setPermissions(
            permissions.stream()
                    .map(PermissionCode::getCode)
                    .collect(Collectors.toList())
    );

    return userRoleDto;
  }

  private UserRoleDto buildUserRoleDto(UserRole userRole) throws AppException {
    User user =
        userRepository
            .findById(userRole.getUserId())
            .orElseThrow(() -> new AppException("User not found"));
    return buildUserRoleDto(userRole, user);
  }

  private User findUserById(Long userId) throws AppException {
    return userRepository.findById(userId)
            .orElseThrow(() -> new AppException("User not found with ID: " + userId));
  }

  private User findUserByUsername(String username) throws AppException {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException("User not found with username: " + username));
  }

  private List<PermissionCode> getPermissionsForRole(Long roleId) throws AppException {
    List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);

    return rolePermissions.stream()
            .map(rp -> {
              try {
                Permission permission = permissionRepository.findById(rp.getPermissionId())
                        .orElse(null);
                return permission != null ? permission.getCode() : null;
              } catch (Exception e) {
                log.warn("Error fetching permission with ID: {}", rp.getPermissionId(), e);
                return null;
              }
            })
            .filter(code -> code != null)
            .collect(Collectors.toList());
  }
}
