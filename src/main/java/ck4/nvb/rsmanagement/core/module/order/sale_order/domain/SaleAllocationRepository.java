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

  /** Find allocations by sale line ID */
  @Query(
      """
    SELECT sa
    FROM SaleAllocation sa
    WHERE sa.saleLineId = :saleLineId
      AND sa.deleted = false
    ORDER BY sa.id
  """)
  List<SaleAllocation> findAllocationsBySaleLineId(@Param("saleLineId") Long saleLineId);

  /** Find allocations by batch stock ID */
  @Query(
      """
    SELECT sa
    FROM SaleAllocation sa
    WHERE sa.batchStockId = :batchStockId
      AND sa.deleted = false
    ORDER BY sa.id
  """)
  List<SaleAllocation> findAllocationsByBatchStockId(@Param("batchStockId") Long batchStockId);

  /** Get total allocated quantity for a batch stock */
  @Query(
      """
    SELECT COALESCE(SUM(sa.qtyAllocated), 0)
    FROM SaleAllocation sa
    WHERE sa.batchStockId = :batchStockId
      AND sa.deleted = false
  """)
  Integer getTotalAllocatedQuantityByBatchStock(@Param("batchStockId") Long batchStockId);

  /** Get total picked quantity for a batch stock */
  @Query(
      """
    SELECT COALESCE(SUM(sa.qtyPicked), 0)
    FROM SaleAllocation sa
    WHERE sa.batchStockId = :batchStockId
      AND sa.deleted = false
  """)
  Integer getTotalPickedQuantityByBatchStock(@Param("batchStockId") Long batchStockId);

  /** Find incomplete allocations (not fully picked) */
  @Query(
      """
    SELECT sa
    FROM SaleAllocation sa
    WHERE sa.qtyPicked < sa.qtyAllocated
      AND sa.deleted = false
    ORDER BY sa.createdTime
  """)
  List<SaleAllocation> findIncompleteAllocations();

  /** Find allocations for a specific product across all stores */
  @Query(
      """
    SELECT sa
    FROM SaleAllocation sa
    JOIN BatchStock bs ON sa.batchStockId = bs.id
    JOIN Batch b ON bs.batchId = b.id
    WHERE b.productId = :productId
      AND sa.deleted = false
      AND bs.deleted = false
      AND b.deleted = false
    ORDER BY sa.createdTime DESC
  """)
  List<SaleAllocation> findAllocationsByProduct(@Param("productId") Long productId);
}
