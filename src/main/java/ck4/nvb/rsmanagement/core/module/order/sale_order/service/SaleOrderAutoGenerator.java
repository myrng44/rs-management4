package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.Customer;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.CustomerRepository;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethod;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethodRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleAllocation;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleAllocationRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleLine;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleLineRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrderRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderCreateDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderGetFullDto;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.VoucherRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchItem;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.BatchItemRepository;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(
    name = "order.generator.enabled",
    havingValue = "true",
    matchIfMissing = false)
public class SaleOrderAutoGenerator {

  private final SaleOrderServiceImpl saleOrderService;
  private final ISaleAllocationService saleAllocationService;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final VoucherRepository voucherRepository;
  private final BatchStockRepository batchStockRepository;
  private final BatchItemRepository batchItemRepository;
  private final SaleLineRepository saleLineRepository;
  private final SaleOrderRepository saleOrderRepository;
  private final SaleAllocationRepository saleAllocationRepository;

  @Value("${order.generator.daily-total:100}")
  private int dailyTotalOrders;

  @Value("${order.generator.realtime-hour-weights}")
  private String realtimeHourWeightsProp;

  @Value("${order.generator.max-realtime-orders}")
  private int maxRealtimeOrders;

  public SaleOrderAutoGenerator(
      SaleOrderServiceImpl saleOrderService,
      ISaleAllocationService saleAllocationService,
      CustomerRepository customerRepository,
      ProductRepository productRepository,
      PaymentMethodRepository paymentMethodRepository,
      VoucherRepository voucherRepository,
      BatchStockRepository batchStockRepository,
      BatchItemRepository batchItemRepository,
      SaleLineRepository saleLineRepository,
      SaleOrderRepository saleOrderRepository,
      SaleAllocationRepository saleAllocationRepository) {
    this.saleOrderService = saleOrderService;
    this.saleAllocationService = saleAllocationService;
    this.customerRepository = customerRepository;
    this.productRepository = productRepository;
    this.paymentMethodRepository = paymentMethodRepository;
    this.voucherRepository = voucherRepository;
    this.batchStockRepository = batchStockRepository;
    this.batchItemRepository = batchItemRepository;
    this.saleLineRepository = saleLineRepository;
    this.saleOrderRepository = saleOrderRepository;
    this.saleAllocationRepository = saleAllocationRepository;
  }

  // ---------------- realtime generator ----------------
  @Scheduled(fixedRateString = "${order.generator.interval}")
  public void scheduledGenerateRealtime() {
    try {
      String prop = realtimeHourWeightsProp == null ? "" : realtimeHourWeightsProp.trim();
      String[] parts = prop.isEmpty() ? new String[0] : prop.split("\\s*,\\s*");
      int[] weights = new int[24];
      boolean parsed = false;
      if (parts.length == 24) {
        parsed = true;
        for (int i = 0; i < 24; i++) {
          try {
            weights[i] = Math.max(0, Integer.parseInt(parts[i]));
          } catch (Exception e) {
            weights[i] = 0;
          }
        }
      }
      if (!parsed) {
        weights =
            new int[] {1, 1, 2, 3, 4, 3, 2, 3, 4, 3, 2, 1, 2, 3, 4, 3, 2, 1, 1, 3, 4, 3, 1, 1};
      }

      int currentHour = LocalDateTime.now().getHour();
      int weight = (currentHour >= 0 && currentHour < weights.length) ? weights[currentHour] : 1;
      int maxWeight = Arrays.stream(weights).max().orElse(1);
      int scaledMaxOrders = 0;
      if (maxWeight > 0) {
        double ratio = weight / (double) maxWeight;
        scaledMaxOrders = 1 + (int) Math.round(ratio * (Math.max(1, maxRealtimeOrders) - 1));
      }
      scaledMaxOrders = Math.max(0, scaledMaxOrders);

      if (scaledMaxOrders <= 0) {
        return;
      }

      int ordersToGenerate = ThreadLocalRandom.current().nextInt(1, scaledMaxOrders + 1);
      for (int i = 0; i < ordersToGenerate; i++) {
        generateOneOrderRealtime();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Transactional
  public void generateOneOrderRealtime() {
    // load base lists
    List<Customer> customers = customerRepository.findAll();
    List<Product> products = productRepository.findAll();
    List<PaymentMethod> payments = paymentMethodRepository.findAll();
    List<Voucher> vouchers = voucherRepository.findAll();

    if (products.isEmpty() || payments.isEmpty()) {
      System.out.println("Skip: not enough base data (products/payments).");
      return;
    }

    // classify customers
    List<Customer> vip = new ArrayList<>();
    List<Customer> regular = new ArrayList<>();
    List<Customer> newcomers = new ArrayList<>();
    for (Customer c : customers) {
      Integer point = c.getPoint() == null ? 0 : c.getPoint();
      if (point >= 1000) vip.add(c);
      else if (point < 50) newcomers.add(c);
      else regular.add(c);
    }

    // derive active stores from batch stocks
    List<BatchStock> activeBatchStocks =
        batchStockRepository.findAll().stream()
            .filter(
                bs ->
                    bs != null
                        && bs.getStatus() != null
                        && "ACTIVE".equalsIgnoreCase(bs.getStatus()))
            .collect(Collectors.toList());
    if (activeBatchStocks.isEmpty()) {
      System.out.println("Skip: no active batch stocks (no store inventory).");
      return;
    }

    List<Long> stores =
        activeBatchStocks.stream()
            .map(BatchStock::getStoreId)
            .distinct()
            .collect(Collectors.toList());
    if (stores.isEmpty()) {
      System.out.println("Skip: no stores found from batch stocks.");
      return;
    }

    double[] storeWeights = new double[stores.size()];
    for (int i = 0; i < stores.size(); i++) {
      storeWeights[i] = Math.max(1.0, 25.0 - i * 3.0);
    }
    Long chosenStoreId = weightedChoiceLong(stores, storeWeights);
    if (chosenStoreId == null) {
      System.out.println("Skip: couldn't choose a store.");
      return;
    }

    // choose customer type and payment weights
    double cp = ThreadLocalRandom.current().nextDouble();
    Customer chosenCustomer = null;
    int avgOrderValueMin = 50_000, avgOrderValueMax = 200_000;
    double[] paymentWeights;
    if (cp < 0.15 && !vip.isEmpty()) {
      chosenCustomer = vip.get(ThreadLocalRandom.current().nextInt(vip.size()));
      avgOrderValueMin = 200_000;
      avgOrderValueMax = 800_000;
      paymentWeights = new double[] {20, 30, 25, 15, 10};
    } else if (cp < 0.65 && !regular.isEmpty()) {
      chosenCustomer = regular.get(ThreadLocalRandom.current().nextInt(regular.size()));
      avgOrderValueMin = 80_000;
      avgOrderValueMax = 300_000;
      paymentWeights = new double[] {40, 25, 20, 10, 5};
    } else if (cp < 0.85 && !newcomers.isEmpty()) {
      chosenCustomer = newcomers.get(ThreadLocalRandom.current().nextInt(newcomers.size()));
      avgOrderValueMin = 50_000;
      avgOrderValueMax = 150_000;
      paymentWeights = new double[] {50, 20, 15, 10, 5};
    } else {
      chosenCustomer = null;
      avgOrderValueMin = 30_000;
      avgOrderValueMax = 120_000;
      paymentWeights = new double[] {70, 15, 10, 3, 2};
    }

    PaymentMethod chosenPayment =
        weightedChoicePayment(paymentMethodRepository.findAll(), paymentWeights);

    boolean maybeUseVoucher =
        ThreadLocalRandom.current().nextDouble()
            < (chosenCustomer != null && vip.contains(chosenCustomer) ? 0.3 : 0.1);
    Voucher chosenVoucher = null;
    if (maybeUseVoucher && !vouchers.isEmpty()) {
      LocalDateTime nowTime = LocalDateTime.now();
      Customer finalChosenCustomer = chosenCustomer;
      List<Voucher> applicable =
          vouchers.stream()
              .filter(
                  v -> {
                    if (v == null) return false;
                    try {
                      LocalDateTime from = v.getValidFrom();
                      LocalDateTime to = v.getValidTo();
                      if (from != null && nowTime.isBefore(from)) return false;
                      if (to != null && nowTime.isAfter(to)) return false;
                    } catch (Exception e) {
                    }
                    String aud = null;
                    try {
                      aud = v.getAudienceType();
                    } catch (Exception e) {
                    }
                    if ("ALL".equalsIgnoreCase(aud)) return true;
                    if ("VIP".equalsIgnoreCase(aud)
                        && finalChosenCustomer != null
                        && vip.contains(finalChosenCustomer)) return true;
                    if ("NEW".equalsIgnoreCase(aud)
                        && finalChosenCustomer != null
                        && newcomers.contains(finalChosenCustomer)) return true;
                    return false;
                  })
              .collect(Collectors.toList());
      if (!applicable.isEmpty())
        chosenVoucher = applicable.get(ThreadLocalRandom.current().nextInt(applicable.size()));
    }

    int numLines = sampleNumLines();

    // get batch items for chosen store
    List<BatchStock> storeBs =
        activeBatchStocks.stream()
            .filter(bs -> bs.getStoreId().equals(chosenStoreId))
            .collect(Collectors.toList());
    if (storeBs.isEmpty()) {
      System.out.println("Store has no active batch stocks, skip.");
      return;
    }
    Set<Long> batchIds = storeBs.stream().map(BatchStock::getBatchId).collect(Collectors.toSet());
    List<BatchItem> availableBatchItems =
        batchItemRepository.findAll().stream()
            .filter(bi -> bi != null && batchIds.contains(bi.getBatchId()))
            .collect(Collectors.toList());
    if (availableBatchItems.isEmpty()) {
      System.out.println("No available batch items for chosen store.");
      return;
    }

    Set<Long> availableProductIds =
        availableBatchItems.stream().map(BatchItem::getProductId).collect(Collectors.toSet());
    List<Product> availableProducts =
        productRepository.findAll().stream()
            .filter(p -> p != null && availableProductIds.contains(p.getId()))
            .collect(Collectors.toList());
    if (availableProducts.isEmpty()) {
      System.out.println("No available products in chosen store.");
      return;
    }

    double[] prodWeights = computeProductWeights(availableProducts);

    List<SaleLineDto> lines = new ArrayList<>();
    int attempts = 0;
    while (lines.size() < numLines && attempts < numLines * 6) {
      attempts++;
      Product chosenProduct = weightedChoiceProduct(availableProducts, prodWeights);
      if (chosenProduct == null) continue;
      long availableQty =
          computeAvailableQtyForProductInStore(chosenProduct.getId(), chosenStoreId);
      if (availableQty <= 0) continue;
      int qty = sampleQtyByCategory(chosenProduct, availableQty);
      if (qty <= 0) continue;
      boolean duplicate =
          lines.stream().anyMatch(l -> l.getProductId().equals(chosenProduct.getId()));
      if (duplicate) continue;
      SaleLineDto l = new SaleLineDto();
      l.setProductId(chosenProduct.getId());
      l.setQtyOrdered(qty);
      l.setUnitPrice(chosenProduct.getUnitPrice());
      lines.add(l);
    }

    if (lines.isEmpty()) {
      System.out.println("No lines created for order (maybe stock low).");
      return;
    }

    SaleOrderCreateDto dto = new SaleOrderCreateDto();
    dto.setCustomerId(chosenCustomer == null ? null : chosenCustomer.getId());
    dto.setVoucherCode(chosenVoucher == null ? null : chosenVoucher.getCode());
    dto.setPaymentId(chosenPayment == null ? null : chosenPayment.getId());
    dto.setLines(lines);

    UserGetDto systemUser = new UserGetDto();
    systemUser.setId(0L);
    systemUser.setUserName("AUTO_GEN");
    systemUser.setStoreId(chosenStoreId);

    try {
      SaleOrderGetFullDto created = saleOrderService.create(dto, systemUser);
      System.out.println(
          "Auto created order id = "
              + created.getId()
              + " store="
              + chosenStoreId
              + " lines="
              + created.getSaleLines().size());
    } catch (AppException ex) {
      System.err.println("Auto-create failed: " + ex.getMessage());
    }
  }

  // ----------------for future generator ----------------
  @Scheduled(cron = "${order.generator.daily-cron}")
  public void scheduledGenerateForNextWeek() {
    LocalDate startDate = LocalDate.now().plusDays(1);
    LocalDate endDate = startDate.plusWeeks(1);

    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
      generateOrdersForDate(date);
    }
  }

  @Transactional
  public void generateOrdersForDate(LocalDate targetDate) {
    List<BatchStock> activeBatchStocks =
        batchStockRepository.findAll().stream()
            .filter(
                bs ->
                    bs != null
                        && bs.getStatus() != null
                        && "ACTIVE".equalsIgnoreCase(bs.getStatus()))
            .collect(Collectors.toList());
    if (activeBatchStocks.isEmpty()) {
      System.out.println("No active batch stocks -> skip daily generation.");
      return;
    }
    List<Long> stores =
        activeBatchStocks.stream()
            .map(BatchStock::getStoreId)
            .distinct()
            .collect(Collectors.toList());
    if (stores.isEmpty()) {
      System.out.println("No stores found -> skip.");
      return;
    }

    double[] hourWeights = new double[24];
    Arrays.fill(hourWeights, 0);
    hourWeights[7] = 4;
    hourWeights[8] = 5;
    hourWeights[9] = 4;
    hourWeights[15] = 3;
    hourWeights[16] = 3;
    hourWeights[20] = 5;
    hourWeights[21] = 5;
    hourWeights[22] = 4;

    double sumWeights = Arrays.stream(hourWeights).sum();
    if (sumWeights <= 0) {
      System.out.println("No hour weights set -> skip.");
      return;
    }

    int totalOrders = Math.max(1, this.dailyTotalOrders);
    int perStoreBase = totalOrders / stores.size();
    int remainder = totalOrders % stores.size();

    for (int si = 0; si < stores.size(); si++) {
      Long storeId = stores.get(si);
      int storeOrders = perStoreBase + (si < remainder ? 1 : 0);
      for (int o = 0; o < storeOrders; o++) {
        int hour = sampleHourByWeights(hourWeights);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);
        LocalDateTime orderTime = LocalDateTime.of(targetDate, LocalTime.of(hour, minute, second));
        try {
          createHistoricalOrderForStoreAtTime(storeId, orderTime);
        } catch (Exception ex) {
          System.err.println(
              "Failed to create historical order for store "
                  + storeId
                  + " at "
                  + orderTime
                  + ": "
                  + ex.getMessage());
        }
      }
    }
  }

  @Transactional
  public void createHistoricalOrderForStoreAtTime(Long storeId, LocalDateTime orderTime)
      throws AppException {
    List<Customer> customers = customerRepository.findAll();
    Customer chosenCustomer = null;
    if (!customers.isEmpty() && ThreadLocalRandom.current().nextDouble() < 0.7) {
      chosenCustomer = customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }

    List<PaymentMethod> payments = paymentMethodRepository.findAll();
    PaymentMethod chosenPayment =
        payments.isEmpty()
            ? null
            : payments.get(ThreadLocalRandom.current().nextInt(payments.size()));

    List<BatchStock> storeBs =
        batchStockRepository.findAll().stream()
            .filter(
                bs ->
                    bs != null
                        && bs.getStoreId() != null
                        && bs.getStoreId().equals(storeId)
                        && "ACTIVE".equalsIgnoreCase(bs.getStatus()))
            .collect(Collectors.toList());
    if (storeBs.isEmpty()) throw new AppException("No active batch stocks for store " + storeId);

    Set<Long> batchIds = storeBs.stream().map(BatchStock::getBatchId).collect(Collectors.toSet());
    List<BatchItem> availableBatchItems =
        batchItemRepository.findAll().stream()
            .filter(bi -> bi != null && batchIds.contains(bi.getBatchId()))
            .collect(Collectors.toList());
    if (availableBatchItems.isEmpty())
      throw new AppException("No batch items available for store " + storeId);

    Set<Long> productIds =
        availableBatchItems.stream().map(BatchItem::getProductId).collect(Collectors.toSet());
    List<Product> availableProducts =
        productRepository.findAll().stream()
            .filter(p -> p != null && productIds.contains(p.getId()))
            .collect(Collectors.toList());
    if (availableProducts.isEmpty()) throw new AppException("No products for store " + storeId);

    int numLines = sampleNumLines();

    SaleOrder order = new SaleOrder();
    order.setStoreId(storeId);
    order.setCustomerId(chosenCustomer == null ? null : chosenCustomer.getId());
    order.setPaymentId(chosenPayment == null ? null : chosenPayment.getId());
    order.setCreatorId(0L);
    order.setCreatedTime(orderTime);
    order.setUpdaterID(0L);
    order.setUpdatedTime(orderTime);
    order.setNew(true);

    int finalPrice = 0;
    SaleOrder savedOrder = saleOrderRepository.save(order);

    int attempts = 0;
    List<SaleLine> createdLines = new ArrayList<>();
    double[] prodWeights = computeProductWeights(availableProducts);
    while (createdLines.size() < numLines && attempts < numLines * 6) {
      attempts++;
      Product p = weightedChoiceProduct(availableProducts, prodWeights);
      if (p == null) continue;
      long availQty = computeAvailableQtyForProductInStore(p.getId(), storeId);
      if (availQty <= 0) continue;
      int qty = sampleQtyByCategory(p, availQty);
      if (qty <= 0) continue;
      boolean dup = createdLines.stream().anyMatch(l -> l.getProductId().equals(p.getId()));
      if (dup) continue;

      SaleLine line = new SaleLine();
      line.setSaleOrderId(savedOrder.getId());
      line.setProductId(p.getId());
      line.setQtyOrdered(qty);
      line.setUnitPrice(p.getUnitPrice());
      line.setCreatedTime(orderTime);
      line.setUpdatedTime(orderTime);
      line.setCreatorId(0L);
      line.setUpdaterID(0L);

      SaleLine savedLine = saleLineRepository.save(line);
      createdLines.add(savedLine);

      finalPrice += p.getUnitPrice() * qty;

      allocateInventoryForSaleLineHistorical(savedLine.getId(), p.getId(), qty, storeId, orderTime);
    }

    savedOrder.setFinalPrice(finalPrice);
    saleOrderRepository.save(savedOrder);
    System.out.println(
        "Generated historical order id="
            + savedOrder.getId()
            + " time="
            + orderTime
            + " lines="
            + createdLines.size());
  }

  private void allocateInventoryForSaleLineHistorical(
      Long saleLineId, Long productId, Integer qtyNeeded, Long storeId, LocalDateTime orderTime)
      throws AppException {
    List<BatchItem> availableBatchItems =
        batchItemRepository.findAvailableByProductAndStoreOrdered(productId, storeId);
    int remaining = qtyNeeded;
    for (BatchItem bi : availableBatchItems) {
      if (remaining <= 0) break;
      int original = bi.getOriginalQty() == null ? 0 : bi.getOriginalQty();
      int sold =
          saleAllocationService.getTotalSoldQuantityByBatchItem(bi.getId()) == null
              ? 0
              : saleAllocationService.getTotalSoldQuantityByBatchItem(bi.getId());
      int avail = Math.max(0, original - sold);
      if (avail <= 0) continue;
      int allocateQty = Math.min(remaining, avail);

      Optional<BatchStock> bsOpt =
          batchStockRepository.findFirstByBatchIdAndStoreId(bi.getBatchId(), storeId);
      Long batchStockId = bsOpt.map(BatchStock::getId).orElse(null);

      if (batchStockId == null) {
        bsOpt =
            batchStockRepository.findAll().stream()
                .filter(
                    bs ->
                        bs != null
                            && bs.getBatchId() != null
                            && bs.getBatchId().equals(bi.getBatchId()))
                .findFirst();
        if (bsOpt.isPresent()) batchStockId = bsOpt.get().getId();
      }

      if (batchStockId == null) {
        throw new AppException(
            "batchStockId not found for batchItem "
                + bi.getId()
                + " (batchId="
                + bi.getBatchId()
                + ", storeId="
                + storeId
                + ")");
      }

      SaleAllocation alloc = new SaleAllocation();
      alloc.setSaleLineId(saleLineId);
      alloc.setBatchItemId(bi.getId());
      alloc.setBatchStockId(batchStockId);
      alloc.setSoldQty(allocateQty);
      alloc.setUnitCostSnap(bi.getImportPrice());
      alloc.setCreatedTime(orderTime);
      alloc.setCreatorId(0L);
      alloc.setUpdatedTime(orderTime);
      alloc.setUpdaterID(0L);

      saleAllocationRepository.save(alloc);

      remaining -= allocateQty;
    }

    if (remaining > 0) {
      throw new AppException(
          "Unable to allocate full qty for product " + productId + ", missing " + remaining);
    }
  }

  // ---------- helpers (shared) ----------
  private double[] computeProductWeights(List<Product> products) {
    double[] w = new double[products.size()];
    for (int i = 0; i < products.size(); i++) {
      Product p = products.get(i);
      if (p == null) {
        w[i] = 1;
        continue;
      }
      try {
        if (p.getUnitPrice() < 50000) w[i] = 35;
        else if (p.getUnitPrice() < 100000) w[i] = 25;
        else if (p.getUnitPrice() < 300000) w[i] = 15;
        else w[i] = 5;
      } catch (Exception ex) {
        w[i] = 5;
      }
    }
    return w;
  }

  private Product weightedChoiceProduct(List<Product> products, double[] weights) {
    if (products == null || products.isEmpty()) return null;
    double total = Arrays.stream(weights).sum();
    if (total <= 0) return products.get(ThreadLocalRandom.current().nextInt(products.size()));
    double r = ThreadLocalRandom.current().nextDouble() * total;
    double acc = 0;
    for (int i = 0; i < products.size(); i++) {
      acc += weights[i];
      if (r <= acc) return products.get(i);
    }
    return products.get(products.size() - 1);
  }

  private PaymentMethod weightedChoicePayment(List<PaymentMethod> payments, double[] baseWeights) {
    if (payments == null || payments.isEmpty()) return null;
    double[] weights = new double[payments.size()];
    if (baseWeights == null || baseWeights.length == 0) {
      Arrays.fill(weights, 1.0);
    } else {
      for (int i = 0; i < payments.size(); i++) {
        if (i < baseWeights.length) weights[i] = Math.max(0.0, baseWeights[i]);
        else weights[i] = baseWeights[baseWeights.length - 1];
      }
    }
    double total = Arrays.stream(weights).sum();
    if (total <= 0) return payments.get(ThreadLocalRandom.current().nextInt(payments.size()));
    double r = ThreadLocalRandom.current().nextDouble() * total;
    double acc = 0;
    for (int i = 0; i < payments.size(); i++) {
      acc += weights[i];
      if (r <= acc) return payments.get(i);
    }
    return payments.get(payments.size() - 1);
  }

  private Long weightedChoiceLong(List<Long> items, double[] weights) {
    if (items == null || items.isEmpty()) return null;
    double total = Arrays.stream(weights).sum();
    double r = ThreadLocalRandom.current().nextDouble() * total;
    double acc = 0;
    for (int i = 0; i < items.size(); i++) {
      acc += weights[i];
      if (r <= acc) return items.get(i);
    }
    return items.get(items.size() - 1);
  }

  private int sampleNumLines() {
    int[] options = {1, 1, 1, 2, 2, 3, 3, 4};
    return options[ThreadLocalRandom.current().nextInt(options.length)];
  }

  private int sampleQtyByCategory(Product p, long available) {
    if (p == null) return 1;
    if (p.getUnitPrice() > 1_000_000) return 1;
    double val = 1.5 + ThreadLocalRandom.current().nextGaussian() * 1.2;
    int qty = Math.max(1, (int) Math.round(val));
    qty = Math.min(qty, (int) Math.max(1, Math.min(available, 10)));
    return qty;
  }

  private int sampleQty(long available) {
    double val = 2.0 + ThreadLocalRandom.current().nextGaussian() * 1.5;
    int qty = (int) Math.round(Math.max(1, val));
    qty = Math.min(qty, (int) Math.max(1, Math.min(available, 20)));
    return qty;
  }

  private long computeAvailableQtyForProductInStore(Long productId, Long storeId) {
    if (productId == null || storeId == null) return 0;
    List<BatchItem> items =
        batchItemRepository.findAvailableByProductAndStoreOrdered(productId, storeId);
    if (items == null || items.isEmpty()) return 0;
    long total = 0;
    for (BatchItem bi : items) {
      Integer original = bi.getOriginalQty() == null ? 0 : bi.getOriginalQty();
      Integer sold = saleAllocationService.getTotalSoldQuantityByBatchItem(bi.getId());
      total += Math.max(0, original - (sold == null ? 0 : sold));
    }
    return total;
  }

  private int sampleHourByWeights(double[] weights) {
    double total = Arrays.stream(weights).sum();
    double r = ThreadLocalRandom.current().nextDouble() * total;
    double acc = 0;
    for (int i = 0; i < weights.length; i++) {
      acc += weights[i];
      if (r <= acc) return i;
    }
    return weights.length - 1;
  }

  // dùng cho test
  public void generateOrdersForDateWithTotal(LocalDate targetDate, int totalOrders) {
    int prev = this.dailyTotalOrders;
    try {
      this.dailyTotalOrders = Math.max(1, totalOrders);
      // gọi phương thức đã có, vẫn trong @Transactional nếu generateOrdersForDate là annotated
      generateOrdersForDate(targetDate);
    } finally {
      this.dailyTotalOrders = prev;
    }
  }

  public void generateOrdersForTomorrow() {
    generateOrdersForDate(LocalDate.now().plusDays(1));
  }
}
