package ck4.nvb.rsmanagement.core.module.stores.stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.Stock;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.StockRepository;
import ck4.nvb.rsmanagement.core.module.stores.stock.service.dto.StockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("stockService")
public class StockServiceImpl extends FullAuditedCrudServiceImpl<StockDto, Stock, Long, UserGetDto, Long> {

    protected StockServiceImpl(StockRepository repository) {
        super(repository, Stock.class);
    }

    @Override
    public StockRepository getRepository() {
        return (StockRepository) super.getRepository();
    }

    @Override
    public StockDto mapToEntityDto(Stock entity) {
        return new ModelMapper().map(entity, StockDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("name", List.of(SearchOperator.EQUALS,
                SearchOperator.CONTAINS));
        keys.put("location", List.of(SearchOperator.EQUALS,
                SearchOperator.CONTAINS));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("name");
        keys.add("location");
        return keys;
    }
}
