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
}
