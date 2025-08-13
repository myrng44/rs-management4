package ck4.nvb.rsmanagement.core.module.order.saleorder.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.saleorder.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.saleorder.domain.SaleOrderRepository;
import ck4.nvb.rsmanagement.core.module.order.saleorder.service.dto.SaleOrderDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("orderService")
public class SaleOrderCrudServiceImpl extends FullAuditedCrudServiceImpl<SaleOrderDto, SaleOrder, String, UserGetDto, Long> {

    protected SaleOrderCrudServiceImpl(SaleOrderRepository repository) {
        super(repository, SaleOrder.class);
    }

    @Override
    public SaleOrderRepository getRepository() {
        return (SaleOrderRepository) super.getRepository();
    }

    @Override
    public SaleOrderDto mapToEntityDto(SaleOrder entity) {
        return new ModelMapper().map(entity, SaleOrderDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("customerId", List.of(SearchOperator.EQUALS));
        keys.put("storeId", List.of(SearchOperator.EQUALS));
        keys.put("voucherId", List.of(SearchOperator.EQUALS));
        keys.put("finalPrice", List.of(SearchOperator.EQUALS,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.GREATER_THAN_OR_EQUAL,
                SearchOperator.LESS_THAN_OR_EQUAL));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("finalPrice");
        return keys;
    }
}
