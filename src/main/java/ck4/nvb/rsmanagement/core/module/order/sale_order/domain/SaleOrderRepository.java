package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public interface SaleOrderRepository extends BaseFullAuditedRepository<SaleOrder, String, Long> {

  int countOrdersByCreatedTimeBetween(LocalDateTime from, LocalDateTime to);

  int countSaleOrdersByCreatedTimeBetweenAndStoreId(LocalDateTime from, LocalDateTime to, Long storeId);

  @Query(
      value =
          """
    SELECT SUM(so.final_price) AS revenue
    FROM sale_order so
    WHERE so.deleted=false
""",
      nativeQuery = true)
  long sumTotalFinalPriceBetween(LocalDateTime from, LocalDateTime to);

  @Query(
          value =
                  """
            SELECT SUM(so.final_price) AS revenue
            FROM sale_order so
            WHERE so.deleted=false
                AND so.store_id=:storeId
        """,
          nativeQuery = true)
  long sumTotalFinalPriceOfAStoreBetween(LocalDateTime from, LocalDateTime to, Long storeId);

  @Query(
      value =
          """
    SELECT COALESCE(SUM(sl.qty_ordered * sl.unit_price), 0)
    FROM sale_line sl
    WHERE sl.sale_order_id = :saleOrderId
""",
      nativeQuery = true)
  int getFinalPriceByOrderId(@Param("saleOrderId") String saleOrderId);
}
