package ck4.nvb.rsmanagement.product.repository;

import ck4.nvb.rsmanagement.product.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    Optional<CategoryEntity> findById(Long id);
}
