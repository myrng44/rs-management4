package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends BaseFullAuditedRepository<Product, Long, Long> {
    @Query(value = """
    SELECT quantity
    FROM store_stock
    WHERE product_id = :productId
      AND store_id = :storeId
      AND deleted = false;
""",
    nativeQuery = true)
    int remainQuantity(Long productId, Long storeId);
}
