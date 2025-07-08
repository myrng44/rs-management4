package ck4.nvb.rsmanagement.repository;

import ck4.nvb.rsmanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
