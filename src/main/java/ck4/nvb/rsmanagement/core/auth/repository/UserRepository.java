package ck4.nvb.rsmanagement.core.auth.repository;

import ck4.nvb.rsmanagement.core.auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);
}
