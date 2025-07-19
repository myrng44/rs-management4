package develop.circlek.repository;

import develop.circlek.entity.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> findByDeletedFalse();

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.rolePermissions rp LEFT JOIN FETCH rp.permission p WHERE r.id = :roleId AND r.deleted = false")
    Optional<RoleEntity> findByIdWithPermissions(@Param("roleId") Long roleId);
}