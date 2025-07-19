package develop.circlek.service.interfacee.user;

import develop.circlek.entity.user.PermissionEntity;
import java.util.List;

public interface PermissionService {
    PermissionEntity createPermission(PermissionEntity permission);
    List<PermissionEntity> getAllPermissions();
    PermissionEntity getPermission(Long id);
    PermissionEntity getPermissionByCode(String code);
    PermissionEntity updatePermission(Long id, PermissionEntity permission);
    void deletePermission(Long id);
}
