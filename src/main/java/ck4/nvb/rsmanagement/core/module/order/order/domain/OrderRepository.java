package ck4.nvb.rsmanagement.core.module.order.order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository("orderRepository")
public interface OrderRepository extends BaseFullAuditedRepository<Orders, String, Long> {
    int countOrdersByCreatedTimeBetween(LocalDateTime from, LocalDateTime to);

    @Query(value = """
    SELECT SUM(o.final_price) AS revenue
    FROM orders o
    WHERE o.deleted=false
""",
    nativeQuery = true)
    long sumTotalFinalPriceBetween(LocalDateTime from, LocalDateTime to);

    @Query(value = """
    SELECT COALESCE(SUM(odt.quantity * odt.unit_price), 0)
    FROM order_detail odt
    WHERE odt.order_id = :orderId
""", nativeQuery = true)
    int getFinalPriceByOrderId(@Param("orderId") String orderId);
}
