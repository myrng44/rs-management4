package ck4.nvb.rsmanagement.core.module.order.sale_return.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("saleReturnRepository")
public interface SaleReturnRepository extends BaseFullAuditedRepository<SaleReturn, Long, Long> {
}
