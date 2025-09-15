package ck4.nvb.rsmanagement.core.module.stores.batch_item.service;

import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItem;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItemRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("bacthItemService")
public class BatchItemServiceImpl extends FullAuditedCrudServiceImpl<BatchItemDto, BatchItem, Long, UserGetDto, Long> implements IBatchItemService {

    protected BatchItemServiceImpl(BatchItemRepository repository) {
        super(repository, BatchItem.class);
    }

    @Autowired private ModelMapper modelMapper;

    @Override
    public BatchItemRepository getRepository() {
        return (BatchItemRepository) super.getRepository();
    }

    @Override
    public BatchItemDto mapToEntityDto(BatchItem entity) {
        return modelMapper.map(entity, BatchItemDto.class);
    }

    @Override
    public BatchItemDto update(Long aLong, UpdateInput<BatchItem> updateDto, UserGetDto user) throws AppException {
        return super.update(aLong, updateDto, user);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        return super.getSearchableKeys();
    }

    @Override
    public Set<String> getSortableKeys() {
        return super.getSortableKeys();
    }

    @Override
    public List<BatchItemDto.WithBatchInfo> findAvailableBatchesInfoForProduct(Long productId, Long storeId) {
        List<Map<String, Object>> result = getRepository().findAvailableBatchesInfoForProduct(productId, storeId);

        return result.stream()
                .map(row -> new BatchItemDto.WithBatchInfo(
                        ((Number) row.get("id")).longValue(),
                        ((Number) row.get("batchId")).longValue(),
                        (String) row.get("batchCode"),
                        (String) row.get("productName"),
                        (String) row.get("supplierName"),
                        ((Number) row.get("originalQty")).intValue(),
                        ((Number) row.get("remainQty")).intValue(),
                        ((Number) row.get("importPrice")).intValue(),
                        ((Timestamp) row.get("manufactureDate")).toLocalDateTime(),
                        ((Timestamp) row.get("expiryDate")).toLocalDateTime()
                )).toList();
    }
}
