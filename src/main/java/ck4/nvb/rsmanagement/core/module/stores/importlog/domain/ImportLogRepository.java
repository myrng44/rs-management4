package ck4.nvb.rsmanagement.core.module.stores.importlog.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("importLogRepository")
public interface ImportLogRepository extends BaseFullAuditedRepository<ImportLog, String, Long> {
}
