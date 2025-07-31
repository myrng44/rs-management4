package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.ImportedProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImportedProductRepository extends BaseRepository<ImportedProductEntity, Long> {
    List<ImportedProductEntity> findByImportLogIdAndDeletedFalse(String importLogId);
    List<ImportedProductEntity> findByProductIdAndDeletedFalse(Long productId);
    List<ImportedProductEntity> findByStatusAndDeletedFalse(String status);

    @Query("SELECT ip FROM ImportedProductEntity ip WHERE ip.expiryDate <= :date AND ip.deleted = false")
    List<ImportedProductEntity> findExpiredProducts(@Param("date") LocalDateTime date);

    @Query("SELECT ip FROM ImportedProductEntity ip WHERE ip.expiryDate BETWEEN :startDate AND :endDate AND ip.deleted = false")
    List<ImportedProductEntity> findProductsExpiringBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}