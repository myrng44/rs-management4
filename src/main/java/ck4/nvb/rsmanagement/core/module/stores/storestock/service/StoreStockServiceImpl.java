package ck4.nvb.rsmanagement.core.module.stores.storestock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStock;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.storestock.service.dto.StoreStockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("storeStockService")
public class StoreStockServiceImpl extends FullAuditedCrudServiceImpl<StoreStockDto, StoreStock, Long, UserGetDto, Long> {

    protected StoreStockServiceImpl(StoreStockRepository repository) {
        super(repository, StoreStock.class);
    }

    @Override
    public StoreStockRepository getRepository() {
        return (StoreStockRepository) super.getRepository();
    }

    @Override
    public StoreStockDto mapToEntityDto(StoreStock entity) {
        return new ModelMapper().map(entity, StoreStockDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("storeId", List.of(SearchOperator.EQUALS, SearchOperator.CONTAINS));
        keys.put("productId", List.of(SearchOperator.EQUALS));
        keys.put("quantity", List.of(SearchOperator.BETWEEN,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("storeId");
        keys.add("productId");
        keys.add("quantity");
        return keys;
    }
}
