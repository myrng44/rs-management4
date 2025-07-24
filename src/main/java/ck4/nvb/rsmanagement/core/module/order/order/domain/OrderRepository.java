package ck4.nvb.rsmanagement.core.module.order.order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public interface OrderRepository extends BaseFullAuditedRepository<Order, String, Long> {
}
