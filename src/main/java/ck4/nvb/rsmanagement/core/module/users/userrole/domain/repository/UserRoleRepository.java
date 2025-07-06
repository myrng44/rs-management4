package ck4.nvb.rsmanagement.core.module.users.userrole.domain.repository;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.users.userrole.domain.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRoleRepository")
public interface UserRoleRepository extends BaseFullAuditedRepository<UserRole, Long, Long> {
    List<UserRole> findByUserId(Long userId);
    List<UserRole> findByStoreId(Long storeId);
    void deleteByUserIdAndRoleIdAndStoreId(Long userId, Long roleId, Long storeId);
}
