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
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.*;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.VoucherRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItem;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItemRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.IBatchItemService;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("orderService")
@Transactional
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
  @Autowired private ISaleAllocationService saleAllocationService;
  @Autowired private ProductRepository productRepository;
  @Autowired private BatchRepository batchRepository;
  @Autowired private BatchStockRepository batchStockRepository;
  @Autowired private IBatchItemService batchItemService;

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
  public SaleOrderGetFullDto create(CreateInput<SaleOrder> createDto, UserGetDto user)
      throws AppException {
    if (createDto instanceof SaleOrderCreateDto) {
      return create((SaleOrderCreateDto) createDto, user);
    }
    return super.create(createDto, user);
  }

  public SaleOrderGetFullDto create(SaleOrderCreateDto createDto, UserGetDto user)
      throws AppException {
    super.checkCreatePermission(createDto, user);

    // check inventory availability first
    validateInventoryAvailability(createDto, user);

    int finalPrice = 0;
    SaleOrder saleOrder = createDto.mapToEntity();
    saleOrder.setStoreId(user.getStoreId());
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
      finalPrice +=
          productRepository
                  .findFirstByIdAndDeletedIsFalse(saleLineDto.getProductId())
                  .getUnitPrice()
              * saleLineDto.getQtyOrdered();
    }
    if (createDto.getVoucherCode() != null) {
      Voucher voucher = voucherRepository.findFirstByCodeAndDeletedFalse(createDto.getVoucherCode());
      saleOrder.setVoucherId(voucher.getId());
      Integer discount = 0;
      if (voucher.getDiscountPer() > 0) {
        discount = (finalPrice * voucher.getDiscountPer()) / 100;
      } else if (voucher.getDiscountVal() != null) {
        discount = voucher.getDiscountVal();
      }
      finalPrice -= discount;
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
      SaleLineGetDto createdLine = saleLineService.create(saleLineDto, user);

      // allocate inventory or this line
      allocateInventoryForSaleLine(
          createdLine.getId(),
          saleLineDto.getProductId(),
          saleLineDto.getQtyOrdered(),
          user);
    }

    return mapToEntityDto(saleOrder);
  }

  /** validate if there's enough inventory available for all products in the order */
  private void validateInventoryAvailability(SaleOrderCreateDto createDto, UserGetDto user) throws AppException {
    for (SaleLineDto lineDto : createDto.getLines()) {
      Long availableQty = getTotalAvailableQuantity(lineDto.getProductId(), user);
      if (availableQty < lineDto.getQtyOrdered()) {
        throw new AppException(
            String.format(
                "Insufficient inventory for product ID %d. Required: %d, Available: %d",
                lineDto.getProductId(), lineDto.getQtyOrdered(), availableQty));
      }
    }
  }

  /** get total available quantity for a product in a specific store */
  private Long getTotalAvailableQuantity(Long productId, UserGetDto user) throws AppException {
    List<BatchItemDto.WithBatchInfo> batchItems = batchItemService.findAvailableBatchesInfoForProduct(productId, user.getStoreId());
    long totalAvailable = 0L;

    for (BatchItemDto.WithBatchInfo batchItem : batchItems) {
      totalAvailable += batchItem.remainQty();
    }

    return totalAvailable;
  }

  /**
   * Get total sold quantity for a specific product from a specific batch_stock
   */
  private Integer getSoldQuantityForBatchItem(Long batchStockId, Long productId) {
    // This would need a custom repository method or query
    // For now, using a simple approach - you might want to optimize this with a custom query
    return saleAllocationService.getTotalSoldQuantityByBatchStockAndProduct(batchStockId, productId);
  }

  /** allocate inventory for a sale line using FIFO strategy */
  private void allocateInventoryForSaleLine(
      Long saleLineId, Long productId, Integer qtyNeeded, UserGetDto user)
      throws AppException {
    List<BatchItemDto.WithBatchInfo> availableBatchItems = batchItemService.findAvailableBatchesInfoForProduct(productId, user.getStoreId());

    int remaining = qtyNeeded;
    for (BatchItemDto.WithBatchInfo bi : availableBatchItems) {
      if (remaining <= 0) break;

      int avail = Math.max(0, bi.remainQty());
      if (avail == 0) continue;

      int allocateQty = Math.min(remaining, avail);

      SaleAllocationDto alloc = new SaleAllocationDto();
      alloc.setSaleLineId(saleLineId);
      alloc.setBatchItemId(bi.id());
      alloc.setSoldQty(allocateQty);
      alloc.setUnitCostSnap(bi.importPrice());
      saleAllocationService.create(alloc, user);

      BatchItemDto updatedRemainQty = new BatchItemDto();

      updatedRemainQty.setImportPrice(bi.importPrice());
      updatedRemainQty.setRemainQty(allocateQty < avail ? avail  - allocateQty : 0);
      batchItemService.update(bi.id(), updatedRemainQty, user);

      remaining -= allocateQty;
      getLogger().info("Allocated {} units from batch_item {} for sale line {}", allocateQty, bi.id(), saleLineId);
    }

    if (remaining > 0) {
      throw new AppException(
              String.format("Unable to fully allocate inventory for product %d. Missing %d units", productId, remaining));
    }
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

  /** STATS METHODS */

  public Long getAllStoreRevenueBetween(LocalDateTime from, LocalDateTime to) throws AppException {
    return getRepository().sumTotalFinalPriceBetween(from, to);
  }

  public Long getAStoreRevenueBetween(LocalDateTime from, LocalDateTime to, Long storeId) throws AppException {
    return getRepository().sumTotalFinalPriceOfAStoreBetween(
            from,
            to,
            storeId
    );
  }

  public int getAllStoreNumberOfOrderBetWeen(LocalDateTime from, LocalDateTime to) throws AppException {
    return getRepository().countOrdersByCreatedTimeBetween(from, to);
  }

  public int getAStoreNumberOfOrderBetween(LocalDateTime from, LocalDateTime to, Long storeId) throws AppException {
    return getRepository().countSaleOrdersByCreatedTimeBetweenAndStoreId(
            from,
            to,
            storeId
    );
  }

  public Long getAverageValuePerOrderBetWeen(LocalDateTime from, LocalDateTime to) throws AppException {
    long revenue = getRepository().sumTotalFinalPriceBetween(from, to);
    int noOrders = getRepository().countOrdersByCreatedTimeBetween(from, to);

    return revenue / noOrders;
  }

  public Long getAverageValuePerOrderOfAStoreBetween(LocalDateTime from, LocalDateTime to, Long storeId) {
    long revenue = getRepository().sumTotalFinalPriceOfAStoreBetween(from, to, storeId);
    int noOrders = getRepository().countSaleOrdersByCreatedTimeBetweenAndStoreId(from, to, storeId);
    return revenue / noOrders;
  }
}
