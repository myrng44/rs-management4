package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends BaseFullAuditedRepository<Product, Long, Long> {
}
