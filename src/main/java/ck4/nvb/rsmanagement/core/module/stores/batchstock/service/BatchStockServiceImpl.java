package ck4.nvb.rsmanagement.core.module.stores.batchstock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("importLogService")
public class BatchStockServiceImpl extends FullAuditedCrudServiceImpl<BatchStockDto, BatchStock, String, UserGetDto, Long> {

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
        keys.put("batchId", List.of(SearchOperator.EQUALS));
        keys.put("storeId", List.of(SearchOperator.EQUALS));
        keys.put("quantity_total", List.of(SearchOperator.EQUALS,
                SearchOperator.GREATER_THAN,
                SearchOperator.LESS_THAN,
                SearchOperator.BETWEEN));
        keys.put("quantity_available", List.of(SearchOperator.EQUALS,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.BETWEEN));
        keys.put("status", List.of(SearchOperator.EQUALS));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("quantity_available");
        keys.add("quantity_reserved");
        return keys;
    }
}
