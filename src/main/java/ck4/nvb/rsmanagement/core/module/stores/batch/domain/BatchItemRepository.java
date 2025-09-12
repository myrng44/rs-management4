package ck4.nvb.rsmanagement.core.module.stores.batch.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("batchItemRepository")
public interface BatchItemRepository extends BaseFullAuditedRepository<BatchItem, Long, Long> {
    List<BatchItem> findByProductId(Long productId);
    List<BatchItem> findByProductIdIn(List<Long> productIds);
    List<BatchItem> findByBatchId(Long batchId);
}
