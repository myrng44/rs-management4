package ck4.nvb.rsmanagement.core.module.users.user.service.impl;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.Permission;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.repository.PermissionRepository;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.Role;
import ck4.nvb.rsmanagement.core.module.users.role.domain.repository.RoleRepository;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.entity.RolePermission;
import ck4.nvb.rsmanagement.core.module.users.rolepermission.domain.repository.RolePermissionRepository;
import ck4.nvb.rsmanagement.core.module.users.user.domain.entity.User;
import ck4.nvb.rsmanagement.core.module.users.user.domain.repository.UserRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserGetServiceWithRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.repository.UserRoleRepository;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.security.service.dto.UserSessionDto;
import ck4.nvb.rsmanagement.core.web.util.CommonPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userGetServiceWithRole")
@RequiredArgsConstructor
public class UserGetServiceWithRoleImpl implements UserGetServiceWithRole {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final CommonPasswordEncoder passwordEncoder;

    @Override
    public UserRoleDto getByUsernameAndPassword(String username, String password) throws AppException {
        User user = authenticateUser(username, password);
        return getPrimaryUserRole(user);
    }

    @Override
    public UserRoleDto getByUsernameAndPasswordAndStore(String username, String password, Long storeId) throws AppException {
        User user = authenticateUser(username, password);
        return getUserRoleForStore(user, storeId);
    }

    @Override
    public UserRoleDto get(Long userId) throws AppException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));
        return getPrimaryUserRole(user);
    }

    @Override
    public UserRoleDto getByUsername(String username) throws AppException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found"));
        return getPrimaryUserRole(user);
    }

    @Override
    public List<UserRoleDto> getAllUserRoles(Long userId) throws AppException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));
        
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream()
                .map(this::buildUserRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserSessionDto getUserSession(Long userId, Long storeId) throws AppException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));
        
        List<UserRoleDto> allRoles = getAllUserRoles(userId);
        UserRoleDto currentRole = allRoles.stream()
                .filter(role -> role.getStoreId().equals(storeId))
                .findFirst()
                .orElseThrow(() -> new AppException("User has no role for this store"));
        
        UserSessionDto session = new UserSessionDto(currentRole);
        session.setUserRoles(allRoles);
        return session;
    }

    private User authenticateUser(String username, String password) throws AppException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException("Invalid password");
        }

        if (!user.isEnabled()) {
            throw new AppException("User is disabled");
        }

        return user;
    }

    private UserRoleDto getPrimaryUserRole(User user) throws AppException {
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        if (userRoles.isEmpty()) {
            throw new AppException("User has no roles assigned");
        }
        
        // Get the first role (primary role)
        UserRole userRole = userRoles.get(0);
        return buildUserRoleDto(userRole, user);
    }

    private UserRoleDto getUserRoleForStore(User user, Long storeId) throws AppException {
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        UserRole userRole = userRoles.stream()
                .filter(ur -> ur.getStoreId().equals(storeId))
                .findFirst()
                .orElseThrow(() -> new AppException("User has no role for this store"));
        
        return buildUserRoleDto(userRole, user);
    }

    private UserRoleDto buildUserRoleDto(UserRole userRole, User user) throws AppException {
        // Get role information
        Role role = roleRepository.findById(userRole.getRoleId())
                .orElseThrow(() -> new AppException("Role not found"));

        // Get permissions for the role
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
        List<String> permissions = rolePermissions.stream()
                .map(rp -> {
                    Permission permission = permissionRepository.findById(rp.getPermissionId()).orElse(null);
                    return permission != null ? permission.getCode() : null;
                })
                .filter(code -> code != null)
                .collect(Collectors.toList());

        // Build UserRoleDto
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setId(userRole.getId());
        userRoleDto.setUserId(user.getId());
        userRoleDto.setRoleId(role.getId());
        userRoleDto.setStoreId(userRole.getStoreId());
        
        // User information
        userRoleDto.setUsername(user.getUsername());
        userRoleDto.setFullName(user.getName());
        userRoleDto.setEmail(user.getEmail());
        userRoleDto.setPhone(user.getPhone());
        
        // Role information
        userRoleDto.setRoleName(role.getName());
        userRoleDto.setRoleDescription(role.getDescription());
        
        // Permissions
        userRoleDto.setPermissions(permissions);

        return userRoleDto;
    }

    private UserRoleDto buildUserRoleDto(UserRole userRole) throws AppException {
        User user = userRepository.findById(userRole.getUserId())
                .orElseThrow(() -> new AppException("User not found"));
        return buildUserRoleDto(userRole, user);
    }
} 