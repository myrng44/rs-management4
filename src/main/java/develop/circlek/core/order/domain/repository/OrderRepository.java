package develop.circlek.core.order.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.CustomerEntity;
import develop.circlek.core.order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity, String> {
    
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE DATE(o.createAt) = CURRENT_DATE AND o.isDeleted = false")
    Long countTodayOrders();

    @Query("SELECT SUM(o.finalPrice) FROM OrderEntity o WHERE MONTH(o.createAt) = MONTH(CURRENT_DATE) AND YEAR(o.createAt) = YEAR(CURRENT_DATE) AND o.isDeleted = false")
    Long getMonthlyRevenue();

    @Query("SELECT c FROM CustomerEntity c WHERE c.id = :customerId AND c.isDeleted = false")
    Optional<CustomerEntity> findCustomerById(@Param("customerId") Long customerId);

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderDetails WHERE o.isDeleted = false ORDER BY o.createAt DESC")
    List<OrderEntity> findAllWithDetails();
}