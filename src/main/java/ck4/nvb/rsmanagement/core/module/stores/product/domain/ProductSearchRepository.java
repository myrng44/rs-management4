package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.search.base.BaseSearchRepository;
import org.springframework.stereotype.Repository;

@Repository("productSearchRepository")
public interface ProductSearchRepository extends BaseSearchRepository<ProductSearchDocument, Long> {
}
