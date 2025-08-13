package ck4.nvb.rsmanagement.core.module.order.saleorder.domain;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
@Repository("saleOrderRepository")
public interface SaleOrderRepository extends BaseFullAuditedRepository<SaleOrder, String, Long> {
    int countOrdersByCreatedTimeBetween(LocalDateTime from, LocalDateTime to);
    @Query(value = """
SELECT SUM(o.final_price) AS revenue
FROM sale_order o
WHERE o.deleted=false
""",
            nativeQuery = true)
    long sumTotalFinalPriceBetween(LocalDateTime from, LocalDateTime to);
}