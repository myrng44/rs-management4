package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.StoreStockEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreStockRepository extends BaseRepository<StoreStockEntity, Long> {
    List<StoreStockEntity> findByStoreIdAndDeletedFalse(Long storeId);
    List<StoreStockEntity> findByProductIdAndDeletedFalse(Long productId);
    Optional<StoreStockEntity> findByStoreIdAndProductIdAndDeletedFalse(Long storeId, Long productId);
    
    @Query("SELECT ss FROM StoreStockEntity ss WHERE ss.quantity <= ss.minQuantity AND ss.deleted = false")
    List<StoreStockEntity> findLowStockItems();
    
    @Query("SELECT ss FROM StoreStockEntity ss LEFT JOIN FETCH ss.product p LEFT JOIN FETCH ss.store s WHERE ss.storeId = :storeId AND ss.deleted = false")
    List<StoreStockEntity> findByStoreIdWithDetails(@Param("storeId") Long storeId);
}