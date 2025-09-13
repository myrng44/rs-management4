package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("saleAllocationRepository")
public interface SaleAllocationRepository
    extends BaseFullAuditedRepository<SaleAllocation, Long, Long> {
  /** Find all allocations for a specific order */
  @Query(
      """
    SELECT sa
    FROM SaleAllocation sa
    JOIN SaleLine sl ON sa.saleLineId = sl.id
    WHERE sl.saleOrderId = :orderId
      AND sa.deleted = false
      AND sl.deleted = false
    ORDER BY sa.id
  """)
  List<SaleAllocation> findAllocationsByOrderId(@Param("orderId") String orderId);

  @Query(value = "SELECT COALESCE(SUM(sa.sold_qty), 0) " +
          "FROM sale_allocation sa " +
          "WHERE sa.batch_item_id = :batchItemId " +
          "  AND sa.deleted = false", nativeQuery = true)
  Integer sumSoldQtyByBatchItemId(@Param("batchItemId") Long batchItemId);

  @Query(value = "SELECT COALESCE(SUM(sa.sold_qty), 0) " +
          "FROM sale_allocation sa " +
          "JOIN batch_item bi ON bi.id = sa.batch_item_id " +
          "JOIN batch b ON b.id = bi.batch_id " +
          "JOIN batch_stock bs ON bs.batch_id = b.id " +
          "JOIN sale_line sl ON sl.id = sa.sale_line_id " +
          "WHERE bs.id = :batchStockId " +
          "  AND sl.product_id = :productId " +
          "  AND sa.deleted = false " +
          "  AND bi.deleted = false " +
          "  AND sl.deleted = false", nativeQuery = true)
  Integer sumSoldQtyByBatchStockAndProduct(@Param("batchStockId") Long batchStockId,
                                           @Param("productId") Long productId);
}
