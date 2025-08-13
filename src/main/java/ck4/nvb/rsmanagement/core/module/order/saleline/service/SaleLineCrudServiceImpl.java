package ck4.nvb.rsmanagement.core.module.order.saleline.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.saleline.domain.SaleLine;
import ck4.nvb.rsmanagement.core.module.order.saleline.domain.SaleLineRepository;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.service.ProductServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("saleLineService")
public class SaleLineCrudServiceImpl extends FullAuditedCrudServiceImpl<SaleLineGetDto, SaleLine, Long, UserGetDto, Long> implements SaleLineService {

    private final ProductServiceImpl productService;

    protected SaleLineCrudServiceImpl(SaleLineRepository repository, ProductServiceImpl productService) {
        super(repository, SaleLine.class);
        this.productService = productService;
    }

    @Override
    public SaleLineRepository getRepository() {
        return (SaleLineRepository) super.getRepository();
    }

    @Override
    public SaleLineGetDto mapToEntityDto(SaleLine entity) {
        Product product = productService.getEntity(entity.getProductId());

        // Snapshot giá tại thời điểm giao dịch
        entity.setUnitPrice(product.getUnitPrice());

        SaleLineGetDto dto = new SaleLineGetDto();
        dto.setId(entity.getId());
        dto.setSaleOrderId(entity.getSaleOrderId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setQuantityAllocated(entity.getQuantityAllocated());
        dto.setQuantityOrdered(entity.getQuantityOrdered());
        dto.setQuantityPicked(entity.getQuantityPicked());
        dto.setUnitPrice(entity.getUnitPrice());
        return dto;
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("saleOrderId", List.of(SearchOperator.EQUALS));
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
        return productService.mapToGetListOutputDto(
                getRepository().findMostSoldProductsOfInterval(start, end, noProducts)
        );
    }

    @Override
    public List<SaleLineGetDto> getDetailByOrderId(String saleOrderId) throws AppException {
        return mapToGetListOutputDto(getRepository().findAllByOrderId(saleOrderId));
    }
}
