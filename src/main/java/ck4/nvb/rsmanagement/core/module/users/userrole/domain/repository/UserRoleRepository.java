package ck4.nvb.rsmanagement.core.module.users.userrole.domain.repository;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.users.role.service.dto.UserRoleProjection;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("userRoleRepository")
public interface UserRoleRepository extends BaseFullAuditedRepository<UserRole, Long, Long> {
  List<UserRole> findByUserId(Long userId);

  List<UserRole> findByUserIdAndStoreId(Long userId, Long storeId);

  @Query(value = """
    SELECT usr.id
    FROM user_role usr
    WHERE usr.store_id=:storeId AND usr.user_id=:userId
""", nativeQuery = true)
  Set<Long> findRoleIdsByUserIdAndStoreId(Long userId, Long storeId);

  Set<Long> findStoreIdsByUserId(Long userId);

  void deleteByUserIdAndRoleIdAndStoreId(Long userId, Long roleId, Long storeId);

  List<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

  @Query(
      value =
          """
    SELECT
      ur.user_id AS userId,
      ur.role_id AS roleId,
      ur.store_id AS storeId,
      u.username AS userName,
      u.full_name AS fullName,
      u.email AS email,
      u.phone AS phone,
      r.name AS roleName,
      STRING_AGG(p.code, ',') AS permissions
    FROM user_role ur
    JOIN users u ON ur.user_id = u.id
    JOIN role r ON ur.role_id = r.id
    LEFT JOIN role_permission rp ON r.id = rp.role_id
    LEFT JOIN permission p ON rp.permission_id = p.id
    WHERE ur.user_id = :userId AND ur.role_id = :roleId
    GROUP BY ur.user_id, ur.role_id, ur.store_id, u.username, u.full_name, u.email, u.phone, r.name
    """,
      nativeQuery = true)
  UserRoleProjection findInfoByUserIdAndRoleId(Long userId, Long roleId);
}
