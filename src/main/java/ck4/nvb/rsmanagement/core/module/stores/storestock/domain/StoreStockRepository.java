package ck4.nvb.rsmanagement.core.module.stores.storestock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("storeStockRepository")
public interface StoreStockRepository extends BaseFullAuditedRepository<StoreStock, Long, Long> {
}
