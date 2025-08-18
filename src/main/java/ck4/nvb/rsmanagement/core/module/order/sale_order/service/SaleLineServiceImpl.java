package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleLine;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleLineRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.service.ProductServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderDetailService")
public class SaleLineServiceImpl
    extends FullAuditedCrudServiceImpl<SaleLineGetDto, SaleLine, Long, UserGetDto, Long>
    implements ISaleLineService {

  @Autowired private ProductServiceImpl productService;

  protected SaleLineServiceImpl(SaleLineRepository repository) {
    super(repository, SaleLine.class);
  }

  @Override
  public SaleLineRepository getRepository() {
    return (SaleLineRepository) super.getRepository();
  }

  @Override
  public SaleLineGetDto mapToEntityDto(SaleLine entity) {
    Product product = productService.getEntity(entity.getProductId());
    // snapshot
    entity.setUnitPrice(
        product.getUnitPrice()); // auto get product's unitPrice at the time of transaction
    SaleLineGetDto dto = new SaleLineGetDto();
    dto.setId(entity.getId());
    dto.setSaleOrderId(entity.getOrderId());
    dto.setProductId(product.getId());
    dto.setProductName(product.getName());
    dto.setQtyOrdered(entity.getQtyOrdered());
    dto.setUnitPrice(entity.getUnitPrice());
    return dto;
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
  public List<ProductGetDto> getMostSoldProductsLastDay(int days, int noProducts)
      throws AppException {
    LocalDateTime end = LocalDateTime.now();
    LocalDateTime start = end.minusDays(days);

    return productService.mapToGetListOutputDto(
        getRepository().findMostSoldProductsOfInterval(start, end, noProducts));
  }

  @Override
  public List<SaleLineGetDto> getDetailByOrderId(String orderId) throws AppException {
    return mapToGetListOutputDto(getRepository().findAllByOrderId(orderId));
  }
}
