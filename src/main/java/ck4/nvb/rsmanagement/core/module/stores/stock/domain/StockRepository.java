package ck4.nvb.rsmanagement.core.module.stores.stock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("stockRepository")
public interface StockRepository extends BaseFullAuditedRepository<Stock, Long, Long> {
}
