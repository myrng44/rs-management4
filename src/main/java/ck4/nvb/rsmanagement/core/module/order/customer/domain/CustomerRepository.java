package ck4.nvb.rsmanagement.core.module.order.customer.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("customerRepository")
public interface CustomerRepository extends BaseFullAuditedRepository<Customer, Long, Long> {
  int countCustomerByDeletedIsFalse();

  @Query(
      value =
          """
    SELECT COUNT(c.id)
    FROM customer c
    WHERE c.deleted=false
        AND c.created_at BETWEEN :start AND :end
    """,
      nativeQuery = true)
  int countCustomerByDeletedIsFalseBetweenInterval(LocalDateTime start, LocalDateTime end);
}
