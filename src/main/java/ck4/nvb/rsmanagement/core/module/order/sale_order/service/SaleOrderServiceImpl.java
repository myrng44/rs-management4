package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.exception.DuplicateIdentifierException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.Customer;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.CustomerRepository;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethod;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethodRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrderRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderCreateDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderGetFullDto;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.VoucherRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class SaleOrderServiceImpl
    extends FullAuditedCrudServiceImpl<SaleOrderGetFullDto, SaleOrder, String, UserGetDto, Long>
    implements ISaleOrderService {

  protected SaleOrderServiceImpl(SaleOrderRepository repository) {
    super(repository, SaleOrder.class);
  }

  @Override
  public SaleOrderRepository getRepository() {
    return (SaleOrderRepository) super.getRepository();
  }

  @Autowired private CustomerRepository customerRepository;
  @Autowired private VoucherRepository voucherRepository;
  @Autowired private PaymentMethodRepository paymentMethodRepository;
  @Autowired private ISaleLineService saleLineService;
  @Autowired private ProductRepository productRepository;

  @Override
  public SaleOrderGetFullDto mapToEntityDto(SaleOrder entity) {
    SaleOrderGetFullDto orderDto = new SaleOrderGetFullDto();
    orderDto.setId(entity.getId());

    if (entity.getCustomerId() != null) {
      Customer customer = customerRepository.findFirstByIdAndDeletedIsFalse(entity.getCustomerId());
      orderDto.setCustomerId(entity.getCustomerId());
      orderDto.setCustomerName(customer.getName());
    }

    List<SaleLineGetDto> lines =
        saleLineService.getAll(
            List.of(new SearchCriteria("saleOrderId", SearchOperator.EQUALS, entity.getId())));
    orderDto.setSaleLines(lines);

    orderDto.setStoreId(entity.getStoreId());
    orderDto.setNote(entity.getNote());

    if (entity.getVoucherId() != null) {
      Voucher voucher = voucherRepository.findFirstByIdAndDeletedIsFalse(entity.getVoucherId());
      orderDto.setVoucherCode(voucher.getCode());
    }

    PaymentMethod paymentMethod = paymentMethodRepository.getReferenceById(entity.getPaymentId());
    orderDto.setPaymentMethodName(paymentMethod.getName());

    orderDto.setFinalPrice(entity.getFinalPrice());
    return orderDto;
  }

  @Override
  public SaleOrderGetFullDto create(CreateInput<SaleOrder> createDto, UserGetDto user) throws AppException {
    if (createDto instanceof SaleOrderCreateDto) {
      return create((SaleOrderCreateDto) createDto, user);
    }
    return super.create(createDto, user);
  }

  public SaleOrderGetFullDto create(SaleOrderCreateDto createDto, UserGetDto user) throws AppException {
    super.checkCreatePermission(createDto, user);
    int finalPrice = 0;
    SaleOrder saleOrder = createDto.mapToEntity();
    saleOrder.setCreatorId(user.getId());
    saleOrder.setCreatedTime(LocalDateTime.now());
    saleOrder.setNew(true);
    saleOrder.setUpdaterID(user.getId());
    saleOrder.setUpdatedTime(saleOrder.getCreatedTime());
    if (saleOrder.getId() != null && exists(saleOrder.getId())) {
      getLogger().error("Duplicate id {}", saleOrder.getId());
      throw new DuplicateIdentifierException("Duplicate identifier " + saleOrder.getId());
    }
    for (SaleLineDto saleLineDto : createDto.getLines()) {
      finalPrice += productRepository.findFirstByIdAndDeletedIsFalse(saleLineDto.getProductId()).getUnitPrice() * saleLineDto.getQtyOrdered();
    }
    saleOrder.setFinalPrice(finalPrice);
    saleOrder = getRepository().save(saleOrder);
    getLogger()
            .info(
                    "Created order id {} by user {}: {}",
                    saleOrder.getId(),
                    saleOrder.getCreatorId(),
                    saleOrder);

    for (SaleLineDto saleLineDto : createDto.getLines()) {
      saleLineDto.setSaleOrderId(saleOrder.getId());
      saleLineService.create(saleLineDto, user);
    }

    return mapToEntityDto(saleOrder);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    keys.put("customerId", List.of(SearchOperator.EQUALS));
    keys.put("storeId", List.of(SearchOperator.EQUALS));
    keys.put("voucherId", List.of(SearchOperator.EQUALS));
    keys.put(
        "finalPrice",
        List.of(
            SearchOperator.EQUALS,
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
