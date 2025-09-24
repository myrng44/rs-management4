package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public interface SaleOrderRepository extends BaseFullAuditedRepository<SaleOrder, String, Long> {

  int countOrdersByCreatedTimeBetween(LocalDateTime from, LocalDateTime to);

  int countSaleOrdersByCreatedTimeBetweenAndStoreId(
          LocalDateTime from, LocalDateTime to, Long storeId);

  // --- tổng doanh thu của tất cả cửa hàng trong khoảng time ---
  @Query(
          value =
                  """
                    SELECT COALESCE(SUM(so.final_price), 0)
                    FROM sale_order so
                    WHERE so.deleted = false
                      AND so.created_at >= :from
                      AND so.created_at < :to
                """,
          nativeQuery = true)
  Long sumTotalFinalPriceBetween(
          @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

  // --- tổng doanh thu của 1 cửa hàng trong khoảng time ---
  @Query(
          value =
                  """
                    SELECT COALESCE(SUM(so.final_price), 0)
                    FROM sale_order so
                    WHERE so.deleted = false
                      AND so.store_id = :storeId
                      AND so.created_at >= :from
                      AND so.created_at < :to
                """,
          nativeQuery = true)
  Long sumTotalFinalPriceOfAStoreBetween(
          @Param("from") LocalDateTime from,
          @Param("to") LocalDateTime to,
          @Param("storeId") Long storeId);

  // final price by order id (giữ nguyên)
  @Query(
          value =
                  """
                    SELECT COALESCE(SUM(sl.qty_ordered * sl.unit_price), 0)
                    FROM sale_line sl
                    WHERE sl.sale_order_id = :saleOrderId
                """,
          nativeQuery = true)
  int getFinalPriceByOrderId(@Param("saleOrderId") String saleOrderId);

  @Query(
          value = """
      SELECT CAST(so.created_at AS date) AS day, COALESCE(SUM(so.final_price), 0) AS revenue
      FROM sale_order so
      WHERE so.deleted = false
        AND so.store_id = :storeId
        AND so.created_at >= :from
        AND so.created_at < :to
      GROUP BY day
      ORDER BY day
    """,
          nativeQuery = true)
  List<Object[]> sumDailyRevenueOfAStoreBetween(
          @Param("from") LocalDateTime from,
          @Param("to") LocalDateTime to,
          @Param("storeId") Long storeId);


  @Query(
          value = """
      SELECT s.id AS store_id, s.name AS store_name, CAST(so.created_at AS date) AS day,
             COALESCE(SUM(so.final_price), 0) AS revenue
      FROM sale_order so
      JOIN store s ON so.store_id = s.id
      WHERE so.deleted = false
        AND so.created_at >= :from
        AND so.created_at < :to
      GROUP BY s.id, s.name, day
      ORDER BY s.id, day
    """,
          nativeQuery = true)
  List<Object[]> sumDailyRevenueAllStoresBetween(
          @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
