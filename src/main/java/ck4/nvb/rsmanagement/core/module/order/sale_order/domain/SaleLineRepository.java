package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
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
        SELECT p.id as id, p.sku as sku, p.name as name, p.description,
               p.unit_price as unitPrice, p.category_id as categoryId,
               top_sold.total_quantity as totalQuantitySold
        FROM product p
        JOIN (
            SELECT sl.product_id, SUM(sl.qty_ordered) AS total_quantity \s
            FROM sale_line sl
            JOIN sale_order so ON sl.sale_order_id = so.id
            WHERE so.created_at BETWEEN :start AND :end
            GROUP BY sl.product_id
            ORDER BY total_quantity DESC
            LIMIT :numberOfProducts
        ) AS top_sold ON p.id = top_sold.product_id
        ORDER BY top_sold.total_quantity DESC
    """,
      nativeQuery = true)
  List<Map<String, Object>> findMostSoldProductsOfIntervalWithQty(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("numberOfProducts") int numberOfProducts);

  @Query(
          value =
                  """
                SELECT p.id as id, p.sku as sku, p.name as name, p.description,
                       p.unit_price as unitPrice, p.category_id as categoryId,
                       top_sold.total_quantity as totalQuantitySold
                FROM product p
                JOIN (
                    SELECT sl.product_id, SUM(sl.qty_ordered) AS total_quantity \s
                    FROM sale_line sl
                    JOIN sale_order so ON sl.sale_order_id = so.id
                    WHERE so.created_at BETWEEN :start AND :end
                        AND so.store_id=:storeId
                    GROUP BY sl.product_id
                    ORDER BY total_quantity DESC
                    LIMIT :numberOfProducts
                ) AS top_sold ON p.id = top_sold.product_id
                ORDER BY top_sold.total_quantity DESC
            """,
          nativeQuery = true)
  List<Map<String, Object>> findMostSoldProductsOfIntervalWithQtyOfAStore(
          @Param("start") LocalDateTime start,
          @Param("end") LocalDateTime end,
          @Param("numberOfProducts") int numberOfProducts,
          @Param("storeId") Long storeId);
}
