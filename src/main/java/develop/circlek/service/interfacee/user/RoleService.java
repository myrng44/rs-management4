package develop.circlek.service.interfacee.user;

import develop.circlek.entity.user.RoleEntity;
import develop.circlek.entity.user.PermissionEntity;
import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleEntity createRole(RoleEntity role);
    RoleEntity createRoleWithPermissions(RoleEntity role, Set<Long> permissionIds);
    List<RoleEntity> getAllRoles();
    List<RoleEntity> getActiveRoles();
    RoleEntity getRole(Long id);
    RoleEntity getRoleByName(String name);
    RoleEntity updateRole(Long id, RoleEntity role);
    void deleteRole(Long id);
    void softDeleteRole(Long id);

    void assignPermissionToRole(Long roleId, Long permissionId);
    void assignPermissionsToRole(Long roleId, Set<Long> permissionIds);
    void removePermissionFromRole(Long roleId, Long permissionId);
    void removeAllPermissionsFromRole(Long roleId);
    Set<PermissionEntity> getRolePermissions(Long roleId);
}