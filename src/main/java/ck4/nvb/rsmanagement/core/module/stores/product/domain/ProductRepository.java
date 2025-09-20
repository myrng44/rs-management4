package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends BaseFullAuditedRepository<Product, Long, Long> {
  @Query(
      value =
          """
    SELECT SUM(COALESCE(bi.remain_qty, 0))
    FROM batch_stock bs
    JOIN batch b ON bs.batch_id=b.id
    JOIN batch_item bi ON bi.batch_id=b.id
    WHERE bi.product_id = :productId
      AND bs.store_id = :storeId
""",
      nativeQuery = true)
  Integer remainQuantity(Long productId, Long storeId);
}
