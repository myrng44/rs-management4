package ck4.nvb.rsmanagement.core.module.stores.importedproduct.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("batchService")
public class BatchCrudServiceImpl extends FullAuditedCrudServiceImpl<BatchDto, Batch, Long, UserGetDto, Long> implements IBatchService {
    
    protected BatchCrudServiceImpl(BatchRepository repository) {
        super(repository, Batch.class);
    }

    @Autowired
    private ModelMapper modelMapper;

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
        keys.put("quantity", List.of(SearchOperator.BETWEEN,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL));
        keys.put("importedPrice", List.of(SearchOperator.BETWEEN,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL));
        keys.put("manufacturingDate", List.of(SearchOperator.BETWEEN,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL,
                SearchOperator.EQUALS));
        keys.put("expiryDate", List.of(SearchOperator.EQUALS,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL,
                SearchOperator.BETWEEN));
        keys.put("currentQuantity", List.of(SearchOperator.BETWEEN,
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
}
