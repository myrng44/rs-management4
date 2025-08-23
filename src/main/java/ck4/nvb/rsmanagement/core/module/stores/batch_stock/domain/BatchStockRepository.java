package ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("importLogRepository")
public interface BatchStockRepository extends BaseFullAuditedRepository<BatchStock, Long, Long> {
    /**
     * Get total available quantity for a product in a specific store
     */
    @Query(value = """
        SELECT COALESCE(SUM(bs.qty_available), 0)
        FROM batch_stock bs
        JOIN batch b ON bs.batch_id = b.id
        WHERE b.product_id = :productId
          AND bs.store_id = :storeId
          AND bs.status = 'ACTIVE'
          AND bs.deleted = false
          AND b.deleted = false
          AND bs.qty_available > 0
        """, nativeQuery = true)
    Long getTotalAvailableQuantityByProductAndStore(@Param("productId") Long productId,
                                                       @Param("storeId") Long storeId);

    /**
     * Find available batch stocks for a product in a store, ordered by expiry date (FIFO)
     */
    @Query(value = """
        SELECT bs.*
        FROM batch_stock bs
        JOIN batch b ON bs.batch_id = b.id
        WHERE b.product_id = :productId
          AND bs.store_id = :storeId
          AND bs.status = 'ACTIVE'
          AND bs.deleted = false
          AND b.deleted = false
          AND bs.qty_available > 0
        ORDER BY b.expiry_date ASC, b.arrival_date ASC
        """, nativeQuery = true)
    List<BatchStock> findAvailableBatchStocksByProductAndStore(@Param("productId") Long productId,
                                                               @Param("storeId") Long storeId);

    /**
     * Find batch stocks that are about to expire (for inventory management)
     */
    @Query(value = """
        SELECT bs.*
        FROM batch_stock bs
        JOIN batch b ON bs.batch_id = b.id
        WHERE bs.store_id = :storeId
          AND bs.status = 'ACTIVE'
          AND bs.deleted = false
          AND b.deleted = false
          AND bs.qty_available > 0
          AND b.expiry_date <= :expiryThreshold
        ORDER BY b.expiry_date ASC
        """, nativeQuery = true)
    List<BatchStock> findExpiringBatchStocks(@Param("storeId") Long storeId,
                                             @Param("expiryThreshold") java.time.LocalDateTime expiryThreshold);

    /**
     * Find batch stocks by batch ID and store ID
     */
    @Query(value = """
        SELECT *
        FROM batch_stock bs
        WHERE bs.batch_id = :batchId
          AND bs.store_id = :storeId
          AND bs.deleted = false
        """, nativeQuery = true)
    BatchStock findByBatchIdAndStoreId(@Param("batchId") Long batchId,
                                       @Param("storeId") Long storeId);

    /**
     * Find batch stocks with low inventory (below threshold)
     */
    @Query(value = """
        SELECT bs.*
        FROM batch_stock bs
        JOIN batch b ON bs.batch_id = b.id
        JOIN product p ON b.product_id = p.id
        WHERE bs.store_id = :storeId
          AND bs.status = 'ACTIVE'
          AND bs.deleted = false
          AND b.deleted = false
          AND p.deleted = false
          AND bs.qty_available <= :threshold
          AND bs.qty_available > 0
        ORDER BY bs.qty_available ASC
        """, nativeQuery = true)
    List<BatchStock> findLowInventoryBatchStocks(@Param("storeId") Long storeId,
                                                 @Param("threshold") Integer threshold);
}
