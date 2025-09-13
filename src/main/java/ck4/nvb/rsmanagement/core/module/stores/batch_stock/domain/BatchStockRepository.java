package ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.util.List;

import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockGetDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("importLogRepository")
public interface BatchStockRepository extends BaseFullAuditedRepository<BatchStock, Long, Long> {
  @Query(value = """
        SELECT bs.*
        FROM batch_stock bs
        WHERE bs.store_id = :storeId
          AND bs.status = :status
          AND bs.deleted = false
    """, nativeQuery = true)
  List<BatchStock> findByStoreIdAndStatusAndDeletedIsFalse(@Param("storeId") Long storeId, @Param("status") String status);

  // tìm batch_stock theo batchId + storeId (giữ, dùng nơi khác)
  @Query(value = """
        SELECT *
        FROM batch_stock bs
        WHERE bs.batch_id = :batchId
          AND bs.store_id = :storeId
          AND bs.deleted = false
        """, nativeQuery = true)
  BatchStock findByBatchIdAndStoreId(@Param("batchId") Long batchId, @Param("storeId") Long storeId);



  /** Find available batch stock infos for a product in a store, order by expiry date (FIFO) */
  @Query(value = """
    SELECT p.name as productName,
            bs.qty_total as qtyTotal,
            bs.qty_available as qtyAvailable,
            bs.qty_reversed as qtyReversed,
            b.batch_code as batchCode,
            spl.name as supplierName,
            b.import_price as importedPrice,
            b.manufacture_date as manufactureDate,
            b.expiry_date as expiryDate
    FROM batch_stock bs
    JOIN batch b ON bs.batch_id = b.id
    JOIN product p ON p.id = b.product_id
    JOIN supplier spl ON spl.id = b.supplier_id
    WHERE b.product_id = :productId
        AND bs.store_id = :storeId
        AND bs.status = 'ACTIVE'
        AND bs.deleted = false
        AND b.deleted = false
        AND bs.qty_available > 0
    ORDER BY b.expiry_date ASC
    """,
  nativeQuery = true)
  List<BatchStockGetDto> findAvailableBatchInfoByProductAndStore(@Param("productId") Long productId, @Param("storeId") Long storeId);
}
