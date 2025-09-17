package ck4.nvb.rsmanagement.core.module.stores.batch.service;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.exception.DuplicateIdentifierException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchGetDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.IBatchItemService;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.IBatchStockService;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.ISupplierService;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("batchService")
public class BatchServiceImpl
    extends FullAuditedCrudServiceImpl<BatchGetDto, Batch, Long, UserGetDto, Long>
    implements IBatchService {

  protected BatchServiceImpl(BatchRepository repository) {
    super(repository, Batch.class);
  }

  @Autowired private ModelMapper modelMapper;
  @Autowired private ISupplierService supplierService;
  @Autowired private IBatchItemService batchItemService;
  @Autowired protected IBatchStockService batchStockService;

  @Override
  public BatchRepository getRepository() {
    return (BatchRepository) super.getRepository();
  }

  @Override
  public BatchGetDto mapToEntityDto(Batch entity) {
    BatchGetDto batchDto = new BatchGetDto();
    batchDto.setId(entity.getId());
    batchDto.setBatchCode(entity.getBatchCode());

    List<BatchItemDto.WithBatchInfo> batchItemDtos =
        batchItemService.findAllByBatchId(entity.getId());
    batchDto.setBatchItems(batchItemDtos);

    return batchDto;
  }

  @Override
  public BatchGetDto create(CreateInput<Batch> createDto, UserGetDto user) throws AppException {
    if (createDto instanceof BatchDto) {
      return create((BatchDto) createDto, user);
    }
    return super.create(createDto, user);
  }

  @Transactional
  public BatchGetDto create(BatchDto createDto, UserGetDto user) throws AppException {
    super.checkCreatePermission(createDto, user);

    Batch batch = createDto.mapToEntity();
    batch.setNew(true);
    batch.setCreatorId(user.getId());
    batch.setCreatedTime(LocalDateTime.now());
    batch.setUpdaterID(user.getId());
    batch.setUpdatedTime(batch.getCreatedTime());

    if (batch.getId() != null || exists(batch.getId())) {
      getLogger().error("Duplicate id {}", batch.getId());
      throw new DuplicateIdentifierException("Duplicate identifier " + batch.getId());
    }

    batch = getRepository().save(batch);
    getLogger()
        .info("Created batch id {} by user {}: {}", batch.getId(), batch.getCreatorId(), batch);
    for (BatchItemDto itemDto : createDto.getBatchItems()) {
      itemDto.setBatchId(batch.getId());
      itemDto.setRemainQty(itemDto.getOriginalQty());

      batchItemService.create(itemDto, user);
    }

    BatchStockDto stockDto = new BatchStockDto();
    stockDto.setBatchId(batch.getId());
    stockDto.setStoreId(user.getStoreId());
    stockDto.setStatus("ACTIVE");
    stockDto.setVersion(1);

    batchStockService.create(stockDto, user);

    return mapToEntityDto(batch);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    keys.put("batchCode", List.of(SearchOperator.EQUALS, SearchOperator.CONTAINS));
    return keys;
  }

  @Override
  public Set<String> getSortableKeys() {
    Set<String> keys = super.getSortableKeys();
    keys.add("batchCode");
    return keys;
  }
}
