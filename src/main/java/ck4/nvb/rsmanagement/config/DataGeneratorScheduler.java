package ck4.nvb.rsmanagement.config;

import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.customer.service.ICustomerService;
import ck4.nvb.rsmanagement.core.module.order.customer.service.dto.CustomerDto;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.IPaymentService;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.dto.PaymentMethodGetDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleOrderService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleLineDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderCreateDto;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.IVoucherService;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.dto.VoucherDto;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.IBatchService;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.IProductService;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.ISupplierService;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.dto.SupplierDto;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.users.user.service.UserGetServiceWithRole;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Extended DataGeneratorScheduler with:
 * - product-level base weights ("essentials")
 * - seasonal multipliers per product (simple keyword-driven)
 * - weighted payment method selection (banking > cash > momo > card)
 * - constrained import price variance for batch creation
 * - weighted pre-compute of product combos
 * NOTE: This file intentionally keeps to the same external service APIs used by the original class.
 */
@Component
@ConditionalOnProperty(value = "rs.data.generator.enabled", havingValue = "true", matchIfMissing = false)
public class DataGeneratorScheduler {
    @Autowired
    private AdjustableClock masterClock;

    private final ThreadLocal<AdjustableClock> threadLocalClock = new ThreadLocal<>();

    private AdjustableClock getThreadClock() {
        AdjustableClock threadClock = threadLocalClock.get();
        if (threadClock == null) {
            synchronized (masterClock) {
                threadClock = new AdjustableClock(masterClock.instant(), masterClock.getZone());
                threadLocalClock.set(threadClock);
            }
        }
        return threadClock;
    }

    @Autowired
    private ISaleOrderService saleOrderService;
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IVoucherService voucherService;
    @Autowired
    private IBatchService batchService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private UserGetServiceWithRole userGetServiceWithRole;

    private final Random random = new Random();

    private static final Long ROLE_MANAGER = 2696833794048002L;
    private static final Long ROLE_STAFF = 2696833794048003L;

    private static final double[] HOUR_WEIGHTS = {
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
            0.02, 0.03, 0.05, 0.08, 0.10, 0.12,
            0.18, 0.10, 0.05, 0.04, 0.08, 0.10,
            0.12, 0.15, 0.06, 0.04, 0.0, 0.0
    };

    private static final String[] custFirstName = {"Nguyễn", "Lê", "Trần", "Phạm", "Kiều", "Tạ", "Vương", "Hoàng", "Vũ", "Dương", "Hà", "Phan", "Trương", "Bùi", "Đặng", "Đỗ", "Ngô", "Hồ", "Đinh", "Lâm", "Mai", "Trịnh", "Đào", "Cao", "Lý", "Lưu", "Lương", "Thái", "Châu", "Tạ", "Phùng", "Tô", "Văn", "Tăng", "Quách", "Lại", "Hứa", "Thạch", "Diệp", "Từ", "Chu", "La", "Đàm", "Tống", "Giang", "Chung", "Triệu"};
    private static final String[] custMidName = {"Thị", "Thu", "Văn", "Trường", "Mạnh", "Đức", "Anh", "Vân", "Phúc", "Quang", "Thùy", "An", "Thanh", "Hoài", "Tùng", "Phương", "Minh", "Ngọc", "Hồng", "Hà", "Nhã", "Tú", "Kim"};
    private static final String[] custLastName = {"Bắc", "Nam", "Đông", "Mạnh", "Sơn", "Mai", "Hưng", "Hiển", "Anh", "Hà", "Linh", "Hương", "Nga", "Trinh", "Vi", "Trang", "Phương", "Nguyên", "Khải", "Thảo", "Huyền", "Ngọc", "Hằng", "Nhung", "Yến", "Hạnh", "Huy", "Tuấn", "Hiếu", "Long", "Hùng", "Bảo", "Cường", "Phúc", "Thằng", "Tùng", "Quân", "Khang", "Hân", "Thư", "Như", "Trân", "Quỳnh", "Trâm", "Châu", "Trúc", "Uyên", "Ý", "Quyên", "Ánh", "Thi", "Thủy", "Oanh", "Xuân", "Băng", "Loan", "Bích", "Nguyệt", "Liên", "Đào"};

    private final ExecutorService orderExecutor = Executors.newFixedThreadPool(20);
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(2);

    private final BlockingQueue<CustomerDto> preGeneratedCustomers = new ArrayBlockingQueue<>(500);
    private volatile boolean customerGenerationRunning = false;

    private int currentVoucherGenMonth = -1;

    private volatile List<ProductGetDto> cachedProducts;
    private volatile List<PaymentMethodGetDto> cachedPayments;
    private volatile List<CustomerDto> cachedCustomers;
    private volatile List<VoucherDto> cachedVouchers;

    // New: product-level base weights and seasonal multipliers
    private volatile Map<Long, Double> productBaseWeight = new ConcurrentHashMap<>();
    private volatile Map<Long, Integer> productBaseImportPrice = new ConcurrentHashMap<>();

    private volatile int[][] preComputedProductCombinations;
    private final int MAX_PRECOMPUTED_COMBOS = 200; // increased combos for more variation

    private LocalDate currentSimulateDate;

    @PostConstruct
    public void initSimDate() {
        this.currentSimulateDate = LocalDate.now(masterClock);
        System.out.println("[DATA-GEN] Starting simulation from date: " + currentSimulateDate);

        startCustomerGeneration();
        // initial preload so weights can be computed
        preloadDataForDay(currentSimulateDate);
    }

    @Async
    void startCustomerGeneration() {
        if (customerGenerationRunning) return;
        customerGenerationRunning = true;

        CompletableFuture.runAsync(() -> {
            UserGetDto sysUser = userGetServiceWithRole.get(2696757125120000L).toUserGetDto();

            while (customerGenerationRunning) {
                try {
                    if (preGeneratedCustomers.size() < 1000) {
                        for (int i = 0; i < 100 && preGeneratedCustomers.size() < 500; i++) {
                            CustomerDto customer = createRandomCustomer();
                            CustomerDto created = customerService.create(customer, sysUser);
                            if (created != null) {
                                preGeneratedCustomers.offer(created);
                            }
                        }
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.err.println("[DATA-GEN][CUSTOMER] Pre-generation failed: " + e.getMessage());
                    try { Thread.sleep(10000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
                }
            }
        });
    }

    private CustomerDto createRandomCustomer() {
        CustomerDto createDto = new CustomerDto();
        String custName = custFirstName[random.nextInt(custFirstName.length)] + " "
                + custMidName[random.nextInt(custMidName.length)] + " "
                + custLastName[random.nextInt(custLastName.length)];
        createDto.setName(custName);
        createDto.setPhone("09" + (10000000 + random.nextInt(90000000)));
        createDto.setGender(random.nextBoolean() ? "M" : "F");
        createDto.setPoint(0);
        return createDto;
    }

    private void buildProductWeightsAndBasePrices() {
        productBaseWeight.clear();
        productBaseImportPrice.clear();
        if (cachedProducts == null) return;

        // Heuristic: essential items get higher base weight. We'll use keywords as signals.
        Map<String, Double> keywordWeights = Map.of(
                "sữa", 3.0,
                "bánh mì", 2.5,
                "nước", 2.5,
                "kem", 1.8,
                "trứng", 2.2,
                "gạo", 3.5,
                "thuốc", 2.8
        );

        for (ProductGetDto p : cachedProducts) {
            String name = p.getName() == null ? "" : p.getName().toLowerCase();
            double weight = 1.0; // default
            for (Map.Entry<String, Double> en : keywordWeights.entrySet()) {
                if (name.contains(en.getKey())) {
                    weight = Math.max(weight, en.getValue());
                }
            }
            // small random jitter so not all essentials equal
            weight = weight * (0.85 + random.nextDouble() * 0.3);
            productBaseWeight.put(p.getId(), weight);

            // base import price heuristic: if product exposes price field use it, else random
            Integer maybePrice = null;
            try {
                // assume product DTO may have getCostPrice or similar; defensive fallback
                Object cost = p.getClass().getMethod("getCostPrice").invoke(p);
                if (cost instanceof Number) maybePrice = ((Number) cost).intValue();
            } catch (Exception ignored) {}
            if (maybePrice == null) {
                // fallback to a reasonable band
                maybePrice = 10000 + random.nextInt(90000);
            }
            productBaseImportPrice.put(p.getId(), maybePrice);
        }
    }

    private int[] distributeOrdersByHourPoisson(int totalOrders, double[] weights) {
        int[] ordersPerHour = new int[24];
        int sum = 0;
        for (int h = 0; h < 24; h++) {
            int val = poissonSample(weights[h] * totalOrders);
            ordersPerHour[h] = val;
            sum += val;
        }
        while (sum < totalOrders) {
            int h = random.nextInt(24);
            ordersPerHour[h]++;
            sum++;
        }
        while (sum > totalOrders) {
            int h = random.nextInt(24);
            if (ordersPerHour[h] > 0) { ordersPerHour[h]--; sum--; }
        }
        return ordersPerHour;
    }

    private int poissonSample(double lambda) {
        if (lambda <= 0) return 0;
        double L = Math.exp(-lambda);
        int k = 0; double p = 1.0;
        do { k++; p *= random.nextDouble(); } while (p > L);
        return k - 1;
    }

    private void generateSaleOrderForStore(LocalDate date, int minOrders, int maxOrders,
                                           List<UserRoleDto> staffUsersOfStore, Long storeId) throws AppException {
        if (staffUsersOfStore == null || staffUsersOfStore.isEmpty()) return;
        if (cachedProducts == null || cachedProducts.isEmpty()) return;
        if (cachedPayments == null || cachedPayments.isEmpty()) return;

        System.out.println("[DATA-GEN][STORE-" + storeId + "] Starting order generation for date: " + date);

        int totalOrders = minOrders + random.nextInt(maxOrders - minOrders + 1);
        int[] ordersPerHour = distributeOrdersByHourPoisson(totalOrders, HOUR_WEIGHTS);

        for (int hour = 0; hour < 24; hour++) {
            if (ordersPerHour[hour] <= 0) continue;
            for (int i = 0; i < ordersPerHour[hour]; i++) {
                try {
                    UserRoleDto userRole = staffUsersOfStore.get(random.nextInt(staffUsersOfStore.size()));
                    UserGetDto user = userRole.toUserGetDto();

                    SaleOrderCreateDto order = createSeasonalWeightedOrder(date, hour, user);

                    int minute = random.nextInt(60);
                    int second = random.nextInt(60);
                    LocalDateTime orderDateTime = date.atTime(hour, minute, second);

                    synchronized (masterClock) {
                        masterClock.reset(orderDateTime);
                        saleOrderService.create(order, user);
                    }

                    if (i == 0) System.out.println("[DATA-GEN][DEBUG] Store " + storeId + " Hour " + hour + ": Order created at " + orderDateTime);

                } catch (Exception e) {
                    System.err.println("[DATA-GEN][ORDER] Failed order in hour " + hour + " for store " + storeId + ": " + e.getMessage());
                }
            }
        }

        System.out.println("[DATA-GEN][STORE-" + storeId + "] Completed " + totalOrders + " orders for date: " + date);
    }

    private SaleOrderCreateDto createSeasonalWeightedOrder(LocalDate date, int hour, UserGetDto user) {
        SaleOrderCreateDto order = new SaleOrderCreateDto();
        order.setNote("Đơn hàng tự động ngày " + date + " giờ " + hour);

        // choose number of lines biased by product weights (essentials => appear more often)
        int numLines = 1 + random.nextInt(3);
        List<SaleLineDto> lines = chooseWeightedProductsAsLines(numLines, date.getMonthValue());
        order.setLines(lines);

        // Weighted payment selection (banking > cash > momo > card)
        PaymentMethodGetDto paymentMethod = chooseWeightedPaymentMethod();
        order.setPaymentId(paymentMethod.getId());

        handleOptimizedCustomerAssignment(order, user);

        if (order.getCustomerId() != null && cachedVouchers != null && !cachedVouchers.isEmpty() && random.nextDouble() < 0.75) {
            VoucherDto voucher = cachedVouchers.get(random.nextInt(cachedVouchers.size()));
            order.setVoucherCode(voucher.getCode());
        }

        return order;
    }

    private List<SaleLineDto> chooseWeightedProductsAsLines(int numLines, int month) {
        List<SaleLineDto> lines = new ArrayList<>();
        if (cachedProducts == null || cachedProducts.isEmpty()) return lines;

        // build weight array combining base weight and seasonal multiplier
        double[] weights = new double[cachedProducts.size()];
        double sum = 0.0;
        for (int i = 0; i < cachedProducts.size(); i++) {
            ProductGetDto p = cachedProducts.get(i);
            double base = productBaseWeight.getOrDefault(p.getId(), 1.0);
            double season = seasonalMultiplierForProduct(p, month);
            double w = Math.max(0.01, base * season);
            weights[i] = w; sum += w;
        }

        // pick without replacement but weighted
        for (int k = 0; k < numLines; k++) {
            int idx = weightedRandomIndex(weights, sum);
            if (idx < 0) break;
            ProductGetDto pick = cachedProducts.get(idx);

            SaleLineDto line = new SaleLineDto();
            line.setProductId(pick.getId());
            // demand multiplier: essentials may sell higher qty
            double baseWeight = productBaseWeight.getOrDefault(pick.getId(), 1.0);
            int qty = 1 + (int) Math.round(random.nextDouble() * (baseWeight > 2.0 ? 4 : 2));
            line.setQtyOrdered(Math.max(1, qty));
            lines.add(line);

            // set weight for chosen index to near zero so not chosen again in same order
            sum -= weights[idx];
            weights[idx] = 0.0;
        }

        return lines;
    }

    private int weightedRandomIndex(double[] weights, double totalSum) {
        if (totalSum <= 0) return -1;
        double r = random.nextDouble() * totalSum;
        double accum = 0.0;
        for (int i = 0; i < weights.length; i++) {
            accum += weights[i];
            if (r <= accum) return i;
        }
        return weights.length - 1;
    }

    private double seasonalMultiplierForProduct(ProductGetDto p, int month) {
        // simple keyword-driven seasonality. Extend as needed.
        String name = p.getName() == null ? "" : p.getName().toLowerCase();
        // Ice cream (kem): months Apr(4) - Aug(8) => +50% - +120%
        if (name.contains("kem") || name.contains("ice cream")) {
            if (month >= 4 && month <= 8) return 1.0 + 0.5 + (random.nextDouble() * 0.7); // 1.5 - 2.2
            else return 0.7 + random.nextDouble() * 0.2; // off-season 0.7 - 0.9
        }
        // Cold/flu medicine increase in winter (Nov-Feb)
        if (name.contains("thuốc") || name.contains("cảm") || name.contains("vitamin")) {
            if (month == 11 || month == 12 || month == 1 || month == 2) return 1.4 + random.nextDouble() * 0.4;
            else return 0.9 + random.nextDouble() * 0.2;
        }
        // Water, beverages: slight increase in summer
        if (name.contains("nước") || name.contains("nước ngọt") || name.contains("nước suối")) {
            if (month >= 4 && month <= 9) return 1.2 + random.nextDouble() * 0.4;
            else return 0.9 + random.nextDouble() * 0.2;
        }
        // default no seasonality
        return 0.9 + random.nextDouble() * 0.3; // 0.9 - 1.2
    }

    private PaymentMethodGetDto chooseWeightedPaymentMethod() {
        // Desired order: banking > cash > momo > card
        // We detect payment type via payment method name/description.
        List<PaymentMethodGetDto> banks = new ArrayList<>();
        List<PaymentMethodGetDto> cash = new ArrayList<>();
        List<PaymentMethodGetDto> momo = new ArrayList<>();
        List<PaymentMethodGetDto> card = new ArrayList<>();
        List<PaymentMethodGetDto> others = new ArrayList<>();

        for (PaymentMethodGetDto pm : cachedPayments) {
            String n = pm.getName() == null ? "" : pm.getName().toLowerCase();
            if (n.contains("bank") || n.contains("transfer") || n.contains("banking") || n.contains("chuyển khoản") || n.contains("atm")) banks.add(pm);
            else if (n.contains("cash") || n.contains("tiền mặt") || n.contains("cash")) cash.add(pm);
            else if (n.contains("momo") || n.contains("zalo") || n.contains("mobile")) momo.add(pm);
            else if (n.contains("card") || n.contains("visa") || n.contains("mastercard") || n.contains("thẻ")) card.add(pm);
            else others.add(pm);
        }

        // Weighted chance buckets
        double bankProb = 0.45; // 45%
        double cashProb = 0.30; // 30%
        double momoProb = 0.15; // 15%
        double cardProb = 0.10; // 10%

        double r = random.nextDouble();
        if (r < bankProb && !banks.isEmpty()) return banks.get(random.nextInt(banks.size()));
        r -= bankProb;
        if (r < cashProb && !cash.isEmpty()) return cash.get(random.nextInt(cash.size()));
        r -= cashProb;
        if (r < momoProb && !momo.isEmpty()) return momo.get(random.nextInt(momo.size()));
        if (!card.isEmpty()) return card.get(random.nextInt(card.size()));
        // Fallback
        List<PaymentMethodGetDto> any = new ArrayList<>(); any.addAll(banks); any.addAll(cash); any.addAll(momo); any.addAll(card); any.addAll(others);
        return any.isEmpty() ? cachedPayments.get(0) : any.get(random.nextInt(any.size()));
    }

    private void handleOptimizedCustomerAssignment(SaleOrderCreateDto order, UserGetDto user) {
        double hasCustomerRand = random.nextDouble();
        if (hasCustomerRand < 0.20) { // 20% orders will have customer
            boolean createNew = random.nextDouble() < 0.03; // 3% create new

            if (createNew) {
                CustomerDto preGenCustomer = preGeneratedCustomers.poll();
                if (preGenCustomer != null) {
                    order.setCustomerId(preGenCustomer.getId());
                } else {
                    assignExistingCustomer(order);
                }
            } else {
                assignExistingCustomer(order);
            }
        } else {
            order.setCustomerId(null); // walk-in customer
        }
    }

    private void assignExistingCustomer(SaleOrderCreateDto order) {
        if (cachedCustomers != null && !cachedCustomers.isEmpty()) {
            CustomerDto customer = cachedCustomers.get(random.nextInt(cachedCustomers.size()));
            order.setCustomerId(customer.getId());
        } else {
            order.setCustomerId(null);
        }
    }

    private void preloadDataForDay(LocalDate date) {
        System.out.println("[DATA-GEN] Preloading data for date: " + date);

        CompletableFuture<Void> productsTask = CompletableFuture.runAsync(() -> {
            cachedProducts = productService.getAll();
            buildProductWeightsAndBasePrices();
            preComputeProductCombinations();
        });

        CompletableFuture<Void> paymentsTask = CompletableFuture.runAsync(() -> { cachedPayments = paymentService.getAll(); });

        CompletableFuture<Void> customersTask = CompletableFuture.runAsync(() -> {
            try { cachedCustomers = customerService.getAll(); } catch (Exception ex) { cachedCustomers = Collections.emptyList(); }
        });

        CompletableFuture<Void> vouchersTask = CompletableFuture.runAsync(() -> {
            try {
                LocalDateTime simulateStart = date.atStartOfDay();
                LocalDateTime simulateEnd = date.atTime(23, 59, 59);
                cachedVouchers = voucherService.getAll(List.of(
                        new SearchCriteria("validFrom", SearchOperator.LESS_THAN_OR_EQUAL, simulateEnd.toString()),
                        new SearchCriteria("validTo", SearchOperator.GREATER_THAN_OR_EQUAL, simulateStart.toString())
                ));
            } catch (Exception ex) { cachedVouchers = Collections.emptyList(); }
        });

        CompletableFuture.allOf(productsTask, paymentsTask, customersTask, vouchersTask).join();
        System.out.println("[DATA-GEN] Data preloading completed for date: " + date);
    }

    private void preComputeProductCombinations() {
        CompletableFuture.runAsync(() -> {
            if (cachedProducts != null && !cachedProducts.isEmpty()) {
                preComputedProductCombinations = new int[MAX_PRECOMPUTED_COMBOS][];
                // Build a weights array for weighted sampling
                double[] weights = new double[cachedProducts.size()]; double sum = 0.0;
                for (int i = 0; i < cachedProducts.size(); i++) {
                    ProductGetDto p = cachedProducts.get(i);
                    double w = productBaseWeight.getOrDefault(p.getId(), 1.0);
                    weights[i] = w; sum += w;
                }

                for (int i = 0; i < MAX_PRECOMPUTED_COMBOS; i++) {
                    int numLines = 1 + random.nextInt(3);
                    int[] combo = new int[numLines];
                    double[] wcopy = Arrays.copyOf(weights, weights.length);
                    double s = sum;
                    for (int j = 0; j < numLines; j++) {
                        int idx = weightedRandomIndex(wcopy, s);
                        if (idx < 0) { combo[j] = random.nextInt(cachedProducts.size()); continue; }
                        combo[j] = idx;
                        s -= wcopy[idx]; wcopy[idx] = 0.0;
                    }
                    preComputedProductCombinations[i] = combo;
                }
            }
        });
    }

    private void checkAndCreateBatchForLowStockPerStore() throws AppException {
        Map<Long, List<UserRoleDto>> managersByStore = groupUsersByRoleByStore(ROLE_MANAGER);
        List<ProductGetDto> products = productService.getAll();
        List<SupplierDto> suppliers = supplierService.getAll();
        if (products.isEmpty() || suppliers.isEmpty()) return;

        LocalDate simDate = this.currentSimulateDate != null ? this.currentSimulateDate : LocalDate.now(masterClock);

        List<CompletableFuture<Void>> storeFutures = managersByStore.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getKey() > 0)
                .map(entry -> CompletableFuture.runAsync(() -> {
                    try {
                        Long storeId = entry.getKey();
                        List<UserRoleDto> managers = entry.getValue();
                        if (managers.isEmpty()) return;

                        UserGetDto manager = managers.get(random.nextInt(managers.size())).toUserGetDto();
                        AdjustableClock threadClock = getThreadClock();

                        List<BatchItemDto> items = new ArrayList<>();
                        for (ProductGetDto product : products) {
                            int remainQty = productService.getRemainQuantity(product.getId(), storeId);
                            if (remainQty < 50) {
                                BatchItemDto item = new BatchItemDto();
                                item.setProductId(product.getId());
                                SupplierDto supplier = suppliers.get(random.nextInt(suppliers.size()));
                                item.setSupplierId(supplier.getId());

                                // Constrain import price variance around base price (few thousand VND at most)
                                int base = productBaseImportPrice.getOrDefault(product.getId(), 20000);
                                int variance = Math.max(1000, (int) (base * 0.1)); // but cap to 10% or at least 1k
                                int delta = random.nextInt(variance * 2 + 1) - variance; // [-variance, +variance]
                                int importPrice = Math.max(1000, base + delta);
                                item.setImportPrice(importPrice);

                                int daysAgo = random.nextInt(31);
                                int manuHour = random.nextInt(24);
                                int manuMinute = random.nextInt(60);
                                int manuSecond = random.nextInt(60);
                                LocalDateTime manuDate = simDate.atTime(manuHour, manuMinute, manuSecond).minusDays(daysAgo);
                                item.setManufactureDate(manuDate);
                                item.setExpiryDate(manuDate.plusDays(180 + random.nextInt(186)));
                                item.setOriginalQty(300);
                                items.add(item);
                            }
                        }

                        if (!items.isEmpty()) {
                            BatchDto batch = new BatchDto();
                            int batchHour = 6 + random.nextInt(16);
                            int batchMinute = random.nextInt(60);
                            int batchSecond = random.nextInt(60);
                            LocalDateTime batchCreateTime = simDate.atTime(batchHour, batchMinute, batchSecond);

                            batch.setBatchCode("BATCH-" + batchCreateTime.toLocalDate() + "-" + random.nextInt(10000));
                            batch.setBatchItems(items);

                            threadClock.reset(batchCreateTime);
                            batchService.create(batch, manager);
                        }
                    } catch (Exception e) {
                        System.err.println("[DATA-GEN][BATCH] Error processing store " + entry.getKey() + ": " + e.getMessage());
                    }
                }, batchExecutor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(storeFutures.toArray(new CompletableFuture[0])).join();
    }

    private void generateMonthlyVouchers(LocalDate date) {
        UserGetDto sysUser = userGetServiceWithRole.get(2696757125120000L).toUserGetDto();
        int month = date.getMonthValue();
        System.out.println("[DATA-GEN][VOUCHER] Generating vouchers for month=" + month);

        for (int i = 0; i < 5; i++) {
            try {
                VoucherDto dto = new VoucherDto();
                dto.setCode("VOUCHER-" + month + "-" + i + "-" + random.nextInt(10000));
                dto.setDescription("Voucher discount percent " + (i + 1) + "of " + month);
                dto.setDiscountPer(5 + random.nextInt(21));
                dto.setDiscountVal(0);
                dto.setValidFrom(LocalDateTime.now(masterClock).minusYears(1));
                dto.setValidTo(LocalDateTime.now(masterClock).plusYears(1));
                dto.setQtyTotal(1000L);
                dto.setQtyRedeemed(0L);
                dto.setAudienceType("NaN");
                voucherService.create(dto, sysUser);
            } catch (Exception ex) { System.err.println("[DATA-GEN][VOUCHER] Failed create voucher: " + ex.getMessage()); }
        }

        for (int i = 0; i < 5; i++) {
            try {
                VoucherDto dto = new VoucherDto();
                dto.setCode("VOUCHER-" + month + "-" + i + "-" + random.nextInt(10000));
                dto.setDescription("Voucher discount value " + (i + 1) + "of " + month);
                dto.setDiscountPer(0);
                dto.setDiscountVal(((5000 + random.nextInt(45001)) / 1000) * 1000);
                dto.setValidFrom(date.withDayOfMonth(1).atStartOfDay());
                dto.setValidTo(date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59));
                dto.setQtyTotal(1000L);
                dto.setQtyRedeemed(0L);
                dto.setPerCustomerLimit(1);
                dto.setAudienceType("NaN");
                voucherService.create(dto, sysUser);
            } catch (Exception ex) { System.err.println("[DATA-GEN][VOUCHER] Failed create voucher: " + ex.getMessage()); }
        }
    }

    @Scheduled(fixedRate = 30000)
    public void scheduledGenerateSaleOrder() {
        System.out.println("[DATA-GEN] ===============================================");
        System.out.println("[DATA-GEN] Starting generation for date: " + currentSimulateDate);

        try {
            if (currentSimulateDate.getMonthValue() != currentVoucherGenMonth) {
                currentVoucherGenMonth = currentSimulateDate.getMonthValue();
                generateMonthlyVouchers(currentSimulateDate);
            }

            preloadDataForDay(currentSimulateDate);

            synchronized (masterClock) {
                masterClock.reset(currentSimulateDate.atStartOfDay());
                System.out.println("[DATA-GEN] Master clock reset to: " + currentSimulateDate.atStartOfDay());
            }

            CompletableFuture<Void> batchFuture = CompletableFuture.runAsync(() -> {
                try { checkAndCreateBatchForLowStockPerStore(); } catch (AppException e) { System.err.println("[DATA-GEN][BATCH] check/create batch failed: " + e.getMessage()); e.printStackTrace(); }
            }, batchExecutor);

            Map<Long, List<UserRoleDto>> staffByStore = groupUsersByRoleByStore(ROLE_STAFF);
            if (staffByStore.isEmpty()) {
                System.out.println("[DATA-GEN] No staff found by store. Skipping order generation.");
                return;
            }

            System.out.println("[DATA-GEN] Found " + staffByStore.size() + " stores to process");

            List<CompletableFuture<Void>> storeFutures = staffByStore.entrySet().parallelStream()
                    .map(entry -> CompletableFuture.runAsync(() -> {
                        try {
                            generateSaleOrderForStore(currentSimulateDate, 290, 410,
                                    entry.getValue(), entry.getKey());
                            System.out.println("[DATA-GEN][ORDER] Completed store: " + entry.getKey());
                        } catch (Exception e) {
                            System.err.println("[DATA-GEN][ORDER] Store " + entry.getKey() + " error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }, orderExecutor))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(storeFutures.toArray(new CompletableFuture[0])).join();
            batchFuture.join();

            System.out.println("[DATA-GEN] Completed simulation for date: " + currentSimulateDate);

            threadLocalClock.remove();

            currentSimulateDate = currentSimulateDate.plusDays(1);

            synchronized (masterClock) { masterClock.reset(currentSimulateDate.atStartOfDay()); }

            System.out.println("[DATA-GEN] Advanced to next date: " + currentSimulateDate);
            System.out.println("[DATA-GEN] ===============================================");

        } catch (Exception e) {
            System.err.println("[DATA-GEN] scheduledGenerateSaleOrder fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<UserRoleDto> getUsersByRole(Long roleId) {
        try { return userGetServiceWithRole.getAllByRoleId(roleId); } catch (AppException e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    private Map<Long, List<UserRoleDto>> groupUsersByRoleByStore(Long roleId) {
        List<UserRoleDto> users = getUsersByRole(roleId);
        return users.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(u -> {
            UserGetDto usr = u.toUserGetDto(); Long storeId = usr.getStoreId(); return storeId == null ? -1L : storeId;
        }));
    }
}
