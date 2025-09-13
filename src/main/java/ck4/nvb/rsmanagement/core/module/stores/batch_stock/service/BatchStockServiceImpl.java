package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockGetDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.InventorySummaryDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("importLogService")
public class BatchStockServiceImpl
    extends FullAuditedCrudServiceImpl<BatchStockDto, BatchStock, Long, UserGetDto, Long> {

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

  public List<BatchStockGetDto> getAllBatchByProduct(Long productId, Long storeId) {
    return getRepository().findAvailableBatchInfoByProductAndStore(productId, storeId);
  }
}
