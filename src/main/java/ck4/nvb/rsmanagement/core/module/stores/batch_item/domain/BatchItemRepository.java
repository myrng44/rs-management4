package ck4.nvb.rsmanagement.core.module.stores.batch_item.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("batchItemRepository")
public interface BatchItemRepository extends BaseFullAuditedRepository<BatchItem, Long, Long> {
    List<BatchItem> findByBatchIdAndProductIdAndDeletedIsFalse(Long batchId, Long productId);

    @Query(value = """ 
            SELECT bi.*
            FROM batch_item bi
            JOIN batch b ON b.id = bi.batch_id
            JOIN batch_stock bs ON bs.batch_id = b.id
            WHERE bi.product_id = :productId
              AND bs.store_id = :storeId
              AND bs.status = 'ACTIVE'
              AND bi.deleted = false
              AND bs.deleted = false
              AND b.deleted = false
            ORDER BY bi.expiry_date ASC NULLS LAST """,
            nativeQuery = true)
    List<BatchItem> findAvailableByProductAndStoreOrdered(Long productId, Long storeId);

    @Query(value = """
    SELECT COALESCE(SUM(bi.original_qty) - SUM(COALESCE(sa_sum.sold_qty, 0)), 0) AS available
    FROM batch_item bi
    JOIN batch b ON b.id = bi.batch_id
    JOIN batch_stock bs ON bs.batch_id = b.id
    LEFT JOIN (
        SELECT batch_item_id, SUM(sold_qty) AS sold_qty
        FROM sale_allocation
        WHERE deleted = false
        GROUP BY batch_item_id
    ) sa_sum ON sa_sum.batch_item_id = bi.id
    WHERE bi.product_id = :productId
      AND bs.store_id = :storeId
      AND bs.status = 'ACTIVE'
      AND bi.deleted = false
      AND bs.deleted = false
      AND b.deleted = false
""", nativeQuery = true)
    Long getTotalAvailableQtyForProductInStore(@Param("productId") Long productId, @Param("storeId") Long storeId);
}
