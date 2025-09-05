package ck4.nvb.rsmanagement.core.module.stores.batch.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository("batchRepository")
public interface BatchRepository extends BaseFullAuditedRepository<Batch, Long, Long> {
    // Lấy tất cả batch cho 1 product
    List<Batch> findByProductId(Long productId);

    // Lấy tất cả batch cho nhiều productIds (IN)
    List<Batch> findByProductIdIn(Collection<Long> productIds);
}