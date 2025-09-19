package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.ProductInventoryDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.IProductService;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("importLogService")
public class BatchStockServiceImpl
    extends FullAuditedCrudServiceImpl<BatchStockDto, BatchStock, Long, UserGetDto, Long>
    implements IBatchStockService {

  @Autowired private IProductService productService;

  protected BatchStockServiceImpl(BatchStockRepository repository) {
    super(repository, BatchStock.class);
  }

  @Override
  public BatchStockRepository getRepository() {
    return (BatchStockRepository) super.getRepository();
  }

  @Override
  public BatchStockDto mapToEntityDto(BatchStock entity) {
    return new ModelMapper().map(entity, BatchStockDto.class);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    return keys;
  }

  @Override
  public Set<String> getSortableKeys() {
    Set<String> keys = super.getSortableKeys();
    return keys;
  }

  @Override
  public ProductInventoryDto getProductInventory(Long productId, Long storeId) {
    ProductInventoryDto productInventoryDto = new ProductInventoryDto();
    ProductGetDto product = productService.get(productId);

    productInventoryDto.setProductName(product.getName());

    List<Map<String, Object>> rawBatchItems =
        getRepository().inventoryListOfAProductInStore(productId, storeId);
    List<BatchItemDto.WithProductStatusInfo> batchItems =
        rawBatchItems.stream()
            .map(
                row ->
                    new BatchItemDto.WithProductStatusInfo(
                        (String) row.get("batchCode"),
                        (String) row.get("supploerName"),
                        ((Number) row.get("originalQty")).intValue(),
                        ((Number) row.get("remainQty")).intValue(),
                        ((Number) row.get("importPrice")).intValue(),
                        ((Timestamp) row.get("manufactureDate")).toLocalDateTime(),
                        ((Timestamp) row.get("expiryDate")).toLocalDateTime()))
            .toList();
    int remainQty = 0;
    for (BatchItemDto.WithProductStatusInfo item : batchItems) {
      remainQty += item.remainQty();
    }
    productInventoryDto.setBatchItems(batchItems);
    productInventoryDto.setRemainStock(remainQty);
    return productInventoryDto;
  }
}
