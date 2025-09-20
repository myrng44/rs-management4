package ck4.nvb.rsmanagement.core.module.users.user.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;

import java.util.List;
import java.util.Optional;

import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.Role;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends BaseFullAuditedRepository<User, Long, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
