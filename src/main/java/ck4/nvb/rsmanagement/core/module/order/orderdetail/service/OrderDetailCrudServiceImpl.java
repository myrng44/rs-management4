package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetailRepository;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailDto;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("orderDetailService")
public class OrderDetailCrudServiceImpl extends FullAuditedCrudServiceImpl<OrderDetailDto, OrderDetail, Long, UserGetDto, Long> implements OrderDetailService {
    protected OrderDetailCrudServiceImpl(OrderDetailRepository repository) {
        super(repository, OrderDetail.class);
    }

    @Override
    public OrderDetailRepository getRepository() {
        return (OrderDetailRepository) super.getRepository();
    }

    @Override
    public OrderDetailDto mapToEntityDto(OrderDetail entity) {
        return new ModelMapper().map(entity, OrderDetailDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("orderId", List.of(SearchOperator.EQUALS));
        keys.put("productId", List.of(SearchOperator.EQUALS));
        return keys;
    }

    @Override
    public List<Product> getMostSoldProductsPerWeek(int noProducts) throws AppException {
        return List.of();
    }
}
