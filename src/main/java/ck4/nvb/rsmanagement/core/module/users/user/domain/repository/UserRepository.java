package ck4.nvb.rsmanagement.core.module.users.user.domain.repository;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.users.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository extends BaseFullAuditedRepository<User, Long, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
