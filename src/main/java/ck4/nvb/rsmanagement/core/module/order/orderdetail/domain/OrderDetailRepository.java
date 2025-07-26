package ck4.nvb.rsmanagement.core.module.order.orderdetail.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("orderDetailRepository")
public interface OrderDetailRepository extends BaseFullAuditedRepository<OrderDetail, Long, Long> {
    @Query(value = """
    SELECT *
    FROM order_detail
    """,
    nativeQuery = true)
    List<OrderDetail> findByOrderId(Long orderId);

    @Query(value = """
    SELECT 
    """,
    nativeQuery = true)
    List<Product> findMostSoldProductsOfInterval(LocalDateTime start, LocalDateTime end, int numberOfProducts);
}
