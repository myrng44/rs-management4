package ck4.nvb.rsmanagement.core.module.order.saleallocation.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleAllocationRepository extends BaseFullAuditedRepository<SaleAllocation, Long, Long> {

}
