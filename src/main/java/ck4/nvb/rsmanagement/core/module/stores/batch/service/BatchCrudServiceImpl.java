package ck4.nvb.rsmanagement.core.module.stores.batch.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("batchService")
public class BatchCrudServiceImpl
        extends FullAuditedCrudServiceImpl<BatchDto, Batch, Long, UserGetDto, Long>
        implements IBatchService {

  protected BatchCrudServiceImpl(BatchRepository repository) {
    super(repository, Batch.class);
  }

  @Autowired private ModelMapper modelMapper;

  @Override
  public BatchRepository getRepository() {
    return (BatchRepository) super.getRepository();
  }

  @Override
  public BatchDto mapToEntityDto(Batch entity) {
    return modelMapper.map(entity, BatchDto.class);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    keys.put(
            "quantity",
            List.of(
                    SearchOperator.BETWEEN,
                    SearchOperator.LESS_THAN,
                    SearchOperator.GREATER_THAN,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN_OR_EQUAL));
    keys.put(
            "importedPrice",
            List.of(
                    SearchOperator.BETWEEN,
                    SearchOperator.LESS_THAN,
                    SearchOperator.GREATER_THAN,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN_OR_EQUAL));
    keys.put(
            "manufacturingDate",
            List.of(
                    SearchOperator.BETWEEN,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN_OR_EQUAL,
                    SearchOperator.EQUALS));
    keys.put(
            "expiryDate",
            List.of(
                    SearchOperator.EQUALS,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN_OR_EQUAL,
                    SearchOperator.BETWEEN));
    keys.put(
            "currentQuantity",
            List.of(
                    SearchOperator.BETWEEN,
                    SearchOperator.GREATER_THAN_OR_EQUAL,
                    SearchOperator.LESS_THAN_OR_EQUAL));
    keys.put("status", List.of(SearchOperator.EQUALS));
    return keys;
  }

  @Override
  public Set<String> getSortableKeys() {
    Set<String> keys = super.getSortableKeys();
    keys.add("quantity");
    keys.add("importedPrice");
    keys.add("manufacturingDate");
    keys.add("expiryDate");
    keys.add("currentQuantity");
    keys.add("status");
    return keys;
  }

  @Override
  public List<BatchDto> findByProductId(Long productId) {
    List<Batch> batches = getRepository().findByProductId(productId);
    return batches.stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public List<BatchDto> findByProductIds(List<Long> productIds) {
    if (productIds == null || productIds.isEmpty()) {
      return List.of();
    }
    List<Batch> batches = getRepository().findByProductIdIn(productIds);
    return batches.stream().map(this::toDto).collect(Collectors.toList());
  }

  // Simple entity -> dto mapping. Nếu bạn có mapper (MapStruct / custom) thì dùng nó.
  private BatchDto toDto(Batch b) {
    BatchDto dto = new BatchDto();
    BeanUtils.copyProperties(b, dto);
    // Nếu tên trường khác (ví dụ importedPrice vs importPrice) thì map thủ công
    return dto;
  }
}