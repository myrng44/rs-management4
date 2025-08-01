package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetailRepository;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.ProductServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("orderDetailService")
public class OrderDetailCrudServiceImpl extends FullAuditedCrudServiceImpl<OrderDetailDto, OrderDetail, Long, UserGetDto, Long> implements OrderDetailService {

    private final ProductServiceImpl productService;

    protected OrderDetailCrudServiceImpl(OrderDetailRepository repository, ProductServiceImpl productService) {
        super(repository, OrderDetail.class);
        this.productService = productService;
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
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("quantity");
        return keys;
    }

    @Override
    public List<ProductGetDto> getMostSoldProductsLastDay(int days, int noProducts) throws AppException {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);

        return productService.mapToGetListOutputDto(getRepository().findMostSoldProductsOfInterval(start, end, noProducts));
    }

    @Override
    public List<OrderDetailDto> getDetailByOrderId(long orderId) throws AppException {
        return List.of();
    }
}
