package ck4.nvb.rsmanagement.core.product.repository;

import ck4.nvb.rsmanagement.core.product.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    Optional<SupplierEntity> findByName(String name);
}
