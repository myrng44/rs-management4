package develop.circlek.core.order.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetailEntity, Long> {
    
    List<OrderDetailEntity> findByOrderId(String orderId);

    @Query(value = """
        SELECT p.id, p.name, p.unit_price, SUM(od.quantity) AS total_sold
        FROM product p
        JOIN order_detail od ON p.id = od.product_id
        JOIN `order` o ON od.order_id = o.id
        WHERE o.create_at >= :startDate
        AND p.deleted = false
        AND o.is_deleted = false
        GROUP BY p.id, p.name, p.unit_price
        ORDER BY SUM(od.quantity) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findTopProductsByDays(@Param("startDate") LocalDateTime startDate, @Param("limit") int limit);

    default List<Object[]> findTopProductsByDays(int days, int limit) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return findTopProductsByDays(startDate, limit);
    }
}