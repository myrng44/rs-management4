package ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("importLogRepository")
public interface BatchStockRepository extends BaseFullAuditedRepository<BatchStock, Long, Long> {

  @Query(
      value =
          """
        SELECT b.batch_code as batchCode,
                spl.name as supplierName,
                bi.original_qty as originalQty,
                bi.remain_qty as remainQty,
                bi.import_price as importPrice,
                bi.manufacture_date as manufactureDate,
                bi.expiry_date as expiryDate
        FROM batch_stock bs
        JOIN batch b ON bs.batch_id = b.id
        JOIN batch_item bi ON bi.batch_id = b.id
        JOIN product p ON p.id = bi.product_id
        JOIN supplier spl ON spl.id = bi.supplier_id
        WHERE p.id = :productId
            AND bs.store_id = :storeId
    """,
      nativeQuery = true)
  List<Map<String, Object>> inventoryListOfAProductInStore(Long productId, Long storeId);
}
