package develop.circlek.core.user.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.user.domain.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends BaseRepository<RolePermissionEntity, Long> {
    List<RolePermissionEntity> findByRoleId(Long roleId);
    List<RolePermissionEntity> findByPermissionId(Long permissionId);
    List<RolePermissionEntity> findByRoleIdIn(Set<Long> roleIds);
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Query("SELECT p.code FROM PermissionEntity p JOIN RolePermissionEntity rp ON p.id = rp.permission.id " +
            "WHERE rp.role.id IN :roleIds")
    List<String> findPermissionCodesByRoleIds(@Param("roleIds") Set<Long> roleIds);
}