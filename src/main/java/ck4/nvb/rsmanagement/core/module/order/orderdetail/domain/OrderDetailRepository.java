package ck4.nvb.rsmanagement.core.module.order.orderdetail.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("orderDetailRepository")
public interface OrderDetailRepository extends BaseFullAuditedRepository<OrderDetail, Long, Long> {
    @Query(value = """
    SELECT *
    FROM order_detail
    WHERE order_id = :orderId
    """,
    nativeQuery = true)
    List<OrderDetail> findAllByOrderId(String orderId);

    @Query(value = """
    SELECT p.*
    FROM product p
    JOIN (
        SELECT od.product_id, SUM(od.quantity) AS total_quantity
        FROM order_detail od
        JOIN orders o ON od.order_id = o.id
        WHERE o.created_time BETWEEN :start AND :end
        GROUP BY od.product_id
        ORDER BY total_quantity DESC
        LIMIT :numberOfProducts
    ) AS top_sold ON p.id = top_sold.product_id
    """,
    nativeQuery = true)
    List<Product> findMostSoldProductsOfInterval(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("numberOfProducts") int numberOfProducts);
}
