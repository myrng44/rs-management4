package ck4.nvb.rsmanagement.core.module.stores.batch_item.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("batchItemRepository")
public interface BatchItemRepository extends BaseFullAuditedRepository<BatchItem, Long, Long> {
  List<BatchItem> findByBatchIdAndProductIdAndDeletedIsFalse(Long batchId, Long productId);

  @Query(
      value =
          """
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

  // Tìm các batch và chi tiết khả dụng cho 1 sản phẩm tại 1 store (FEFO)
  @Query(
      value =
          """
                SELECT  bi.id,
                        bi.batch_id as batchId,
                        b.batch_code as batchCode,
                       p.name AS productName,
                       s.name AS supplierName,
                       bi.original_qty as originalQty,
                       bi.remain_qty as remainQty,
                       bi.manufacture_date as manufactureDate,
                       bi.expiry_date as expiryDate,
                       bi.import_price as importPrice,
                       bs.created_at
                FROM batch_item bi
                         JOIN batch_stock bs ON bs.batch_id = bi.batch_id
                         JOIN batch b ON b.id = bi.batch_id
                         JOIN product p ON p.id = bi.product_id
                         JOIN supplier s ON s.id = bi.supplier_id
                WHERE bi.product_id = :productId
                  AND bs.store_id = :storeId
                  AND bs.status = 'ACTIVE'
                  AND bs.deleted = false
                  AND bi.deleted = false
                  AND b.deleted = false
                  AND bi.remain_qty > 0
                ORDER BY bi.expiry_date ASC NULLS LAST, bs.created_at ASC
                    """,
      nativeQuery = true)
  List<Map<String, Object>> findAvailableBatchesInfoForProduct(
      @Param("productId") long productId, @Param("storeId") long storeId);

  @Query(
      value =
          """
                    SELECT  bi.id,
                        bi.batch_id as batchId,
                        b.batch_code as batchCode,
                       p.name AS productName,
                       s.name AS supplierName,
                       bi.original_qty as originalQty,
                       bi.remain_qty as remainQty,
                       bi.manufacture_date as manufactureDate,
                       bi.expiry_date as expiryDate,
                       bi.import_price as importPrice,
                       bs.created_at
                FROM batch_item bi
                         JOIN batch_stock bs ON bs.batch_id = bi.batch_id
                         JOIN batch b ON b.id = bi.batch_id
                         JOIN product p ON p.id = bi.product_id
                         JOIN supplier s ON s.id = bi.supplier_id
                WHERE bi.batch_id = :batchId
            """,
      nativeQuery = true)
  List<Map<String, Object>> findAllByBatchId(Long batchId);
}
