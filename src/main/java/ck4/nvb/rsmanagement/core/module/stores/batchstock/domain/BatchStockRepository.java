package ck4.nvb.rsmanagement.core.module.stores.batchstock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.domain.BatchStock;
import org.springframework.stereotype.Repository;

@Repository("importLogRepository")
public interface BatchStockRepository extends BaseFullAuditedRepository<BatchStock, Long, Long> {}