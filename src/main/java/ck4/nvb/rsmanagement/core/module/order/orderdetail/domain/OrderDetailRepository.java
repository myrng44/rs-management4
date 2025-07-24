package ck4.nvb.rsmanagement.core.module.order.orderdetail.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("orderDetailRepository")
public interface OrderDetailRepository extends BaseFullAuditedRepository<OrderDetail, Long, Long> {
}
