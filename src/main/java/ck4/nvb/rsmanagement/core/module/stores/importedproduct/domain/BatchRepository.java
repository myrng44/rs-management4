package ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("batchRepository")
public interface BatchRepository extends BaseFullAuditedRepository<Batch, Long, Long> {
}
