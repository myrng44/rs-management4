package develop.circlek.repository;

import develop.circlek.entity.user.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    List<UserRoleEntity> findByUserId(Long userId);

    List<UserRoleEntity> findByRoleId(Long roleId);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndRoleId(Long userId, Long roleId);

    @Query("SELECT ur FROM UserRoleEntity ur LEFT JOIN FETCH ur.role r LEFT JOIN FETCH r.rolePermissions rp LEFT JOIN FETCH rp.permission p WHERE ur.userId = :userId")
    List<UserRoleEntity> findByUserIdWithPermissions(@Param("userId") Long userId);
}