package ck4.nvb.rsmanagement.core.module.order.saleline.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("orderDetailRepository")
public interface SaleLineRepository extends BaseFullAuditedRepository<SaleLine, Long, Long> {
  @Query(
      value = """
    SELECT *
    FROM sale_line
    WHERE sale_order_id = :saleOrderId
    """,
      nativeQuery = true)
  List<SaleLine> findBySaleOrderId(String saleOrderId);

  @Query(
      value =
          """
    SELECT p.*
    FROM product p
    JOIN (
        SELECT sl.product_id, SUM(sl.qty_ordered) AS total_quantity
        FROM sale_line sl
        JOIN sale_order so ON sl.sale_order_id = so.id
        WHERE so.created_at BETWEEN :start AND :end
        GROUP BY sl.product_id
        ORDER BY total_quantity DESC
        LIMIT :numberOfProducts
    ) AS top_sold ON p.id = top_sold.product_id
    """,
      nativeQuery = true)
  List<Product> findMostSoldProductsOfInterval(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("numberOfProducts") int numberOfProducts);
}