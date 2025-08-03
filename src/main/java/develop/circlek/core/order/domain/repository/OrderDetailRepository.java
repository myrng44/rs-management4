package develop.circlek.core.order.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetailEntity, Long> {
    
    List<OrderDetailEntity> findByOrderId(String orderId);

    @Query(value = "SELECT p.* FROM product p JOIN order_detail od ON p.id = od.product_id " +
           "JOIN `order` o ON od.order_id = o.id " +
           "WHERE o.created_at >= DATE_SUB(NOW(), INTERVAL :days DAY) AND o.is_deleted = false " +
           "GROUP BY p.id ORDER BY SUM(od.quantity) DESC LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopProductsByDays(@Param("days") int days, @Param("limit") int limit);
}