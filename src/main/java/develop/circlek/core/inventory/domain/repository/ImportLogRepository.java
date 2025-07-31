package develop.circlek.core.inventory.domain.repository;

import develop.circlek.core.inventory.domain.entity.ImportLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImportLogRepository extends JpaRepository<ImportLogEntity, String> {
    List<ImportLogEntity> findByDeletedFalse();
    List<ImportLogEntity> findByToStoreIdAndDeletedFalse(Long storeId);
    List<ImportLogEntity> findByFromStockIdAndDeletedFalse(Long stockId);
    List<ImportLogEntity> findByStatusAndDeletedFalse(Boolean status);
    
    @Query("SELECT il FROM ImportLogEntity il LEFT JOIN FETCH il.importedProducts ip WHERE il.id = :id AND il.deleted = false")
    Optional<ImportLogEntity> findByIdWithProducts(@Param("id") String id);
    
    @Query("SELECT il FROM ImportLogEntity il WHERE il.startDate BETWEEN :startDate AND :endDate AND il.deleted = false")
    List<ImportLogEntity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}