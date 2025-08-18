package ck4.nvb.rsmanagement.core.module.order.sale_order.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("saleAllocationRepository")
public interface SaleAllocationRepository extends BaseFullAuditedRepository<SaleAllocation, Long, Long> {
}
