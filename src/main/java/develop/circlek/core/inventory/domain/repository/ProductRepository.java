package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity, Long> {
    List<ProductEntity> findByDeletedFalse();
    Optional<ProductEntity> findBySkuAndDeletedFalse(String sku);
    List<ProductEntity> findByCategoryIdAndDeletedFalse(Long categoryId);
    List<ProductEntity> findBySupplierIdAndDeletedFalse(Long supplierId);

    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.category c LEFT JOIN FETCH p.supplier s WHERE p.id = :id AND p.deleted = false")
    Optional<ProductEntity> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM ProductEntity p WHERE p.name LIKE %:name% AND p.deleted = false")
    List<ProductEntity> findByNameContainingAndDeletedFalse(@Param("name") String name);
}