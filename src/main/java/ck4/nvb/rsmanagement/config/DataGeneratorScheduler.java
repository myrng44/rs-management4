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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "rs.data.generator.enabled", havingValue = "true", matchIfMissing = false)
public class DataGeneratorScheduler {
    @Autowired
    private AdjustableClock clock;
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
            0.0,0.0,0.0,0.0,0.0,0.0, // 0h-5h (close)
            0.02, // 6h
            0.03, // 7h
            0.05, // 8h
            0.08, // 9h
            0.10, // 10h
            0.12, // 11h
            0.18, // 12h (luch peek)
            0.10, // 13h
            0.05, // 14h
            0.04, // 15h
            0.08, // 16h
            0.10, // 17h
            0.12, // 18h
            0.15, // 19h (dinner peek)
            0.06, // 20h
            0.04, // 21h
            0.0,0.0 // 22h-23h (close)
    };

    private static final String[] custFirstName = {"Nguyễn", "Lê", "Trần", "Phạm", "Kiều", "Tạ", "Vương", "Hoàng", "Vũ", "Dương", "Hà", "Phan", "Trương", "Bùi", "Đặng", "Đỗ", "Ngô", "Hồ", "Đinh", "Lâm", "Mai", "Trịnh", "Đào", "Cao", "Lý", "Lưu", "Lương", "Thái", "Châu", "Tạ", "Phùng", "Tô", "Văn", "Tăng", "Quách", "Lại", "Hứa", "Thạch", "Diệp", "Từ", "Chu", "La", "Đàm", "Tống", "Giang", "Chung", "Triệu"};
    private static final String[] custMidName = {"Thị", "Thu", "Văn", "Trường", "Mạnh", "Đức", "Anh", "Vân", "Phúc", "Quang", "Thùy", "An", "Thanh", "Hoài", "Tùng", "Phương", "Minh", "Ngọc", "Hồng", "Hà", "Nhã", "Tú", "Kim"};
    private static final String[] custLastName = {"Bắc", "Nam", "Đông", "Mạnh", "Sơn", "Mai", "Hưng", "Hiển", "Anh", "Hà", "Linh", "Hương", "Nga", "Trinh", "Vi", "Trang", "Phương", "Nguyên", "Khải", "Thảo", "Huyền", "Ngọc", "Hằng", "Nhung", "Yến", "Hạnh", "Huy", "Tuấn", "Hiếu", "Long", "Hùng", "Bảo", "Cường", "Phúc", "Thằng", "Tùng", "Quân", "Khang", "Hân", "Thư", "Như", "Trân", "Quỳnh", "Trâm", "Châu", "Trúc", "Uyên", "Ý", "Quyên", "Ánh", "Thi", "Thủy", "Oanh", "Xuân", "Băng", "Loan", "Bích", "Nguyệt", "Liên", "Đào"};

    // Configurable pool size
    private final ExecutorService orderExecutor = Executors.newFixedThreadPool(8);
    private final ExecutorService batchExecutor = Executors.newSingleThreadExecutor();

    // Voucher generation tracking
    private int currentVoucherGenMonth = -1;

    private List<UserRoleDto> getUsersByRole(Long roleId) {
        try {
            return userGetServiceWithRole.getAllByRoleId(roleId);
        } catch (AppException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Map<Long, List<UserRoleDto>> groupUsersByRoleByStore(Long roleId) {
        List<UserRoleDto> users = getUsersByRole(roleId);
        return users.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(u -> {
                    UserGetDto usr = u.toUserGetDto();
                    Long storeId = usr.getStoreId();
                    return storeId == null ? -1L : storeId;
                }));
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
            if (ordersPerHour[h] > 0) {
                ordersPerHour[h]--;
                sum--;
            }
        }
        return ordersPerHour;
    }

    private int poissonSample(double lambda) {
        if (lambda <= 0) return 0;
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p *= random.nextDouble();
        } while (p > L);
        return k - 1;
    }

    //Generate sale orders for a single store
    private void generateSaleOrderForStore(LocalDate date, int minOrders, int maxOrders, List<UserRoleDto> staffUsersOfStore) throws AppException {
        if (staffUsersOfStore == null || staffUsersOfStore.isEmpty()) return;
        List<ProductGetDto> products = productService.getAll();
        List<PaymentMethodGetDto> paymentMethods = paymentService.getAll();
        if (products.isEmpty()) {
            System.out.println("[DATA-GEN][ORDER] No products -> skipping orders for this store.");
            return;
        }
        if (paymentMethods.isEmpty()) {
            System.out.println("[DATA-GEN][ORDER] No payment methods -> skipping orders for this store.");
            return;
        }

        List<CustomerDto> customers = Collections.emptyList();
        try {
            customers = customerService.getAll();
        } catch (Exception ex) {
            System.out.println("[DATA-GEN][ORDER] unable to fetch customers: " + ex.getMessage());
        }

        int totalOrders = minOrders + random.nextInt(maxOrders - minOrders + 1);
        int[] ordersPerHour = distributeOrdersByHourPoisson(totalOrders, HOUR_WEIGHTS);

        for (int hour = 0; hour < 24; hour++) {
            for (int i = 0; i < ordersPerHour[hour]; i++) {
                UserRoleDto userRole = staffUsersOfStore.get(random.nextInt(staffUsersOfStore.size()));
                UserGetDto user = userRole.toUserGetDto();

                int numLines = 1 + random.nextInt(3);
                List<SaleLineDto> lines = new ArrayList<>();
                for (int j = 0; j < numLines; j++) {
                    ProductGetDto product = products.get(random.nextInt(products.size()));
                    SaleLineDto line = new SaleLineDto();
                    line.setProductId(product.getId());
                    line.setQtyOrdered(1 + random.nextInt(5));
                    lines.add(line);
                }

                PaymentMethodGetDto paymentMethod = paymentMethods.get(random.nextInt(paymentMethods.size()));
                SaleOrderCreateDto order = new SaleOrderCreateDto();
                order.setLines(lines);
                order.setNote("Đơn hàng tự động ngày " + date + " giờ " + hour);
                order.setPaymentId(paymentMethod.getId());
                // --- Customer logic: 20% have customerId; within them 3% must create new customer ---
                double hasCustomerRand = random.nextDouble();
                if (hasCustomerRand < 0.20) { // 20% orders will have customer
                    boolean createNew = random.nextDouble() < 0.03; // 3% of orders -> create new customer
                    if (createNew) {
                        try {
                            CustomerDto createDto = new CustomerDto();
                            String custName = custFirstName[random.nextInt(custFirstName.length)] + " "
                                    + custMidName[random.nextInt(custMidName.length)] + " "
                                    + custLastName[random.nextInt(custLastName.length)];
                            createDto.setName(custName);
                            createDto.setPhone("09" + (10000000 + random.nextInt(90000000)));
                            boolean gender = random.nextBoolean();
                            createDto.setGender(gender ? "M" : "F");
                            createDto.setPoint(0);
                            CustomerDto created = customerService.create(createDto, user);
                            if (created != null) {
                                order.setCustomerId(created.getId());
                            } else {
                                // fallback to null if create failed silently
                                order.setCustomerId(null);
                            }
                        } catch (Exception ex) {
                            System.err.println("[DATA-GEN][ORDER] create customer failed: " + ex.getMessage());
                            order.setCustomerId(null);
                        }
                    } else {
                        // pick existing if available
                        if (customers != null && !customers.isEmpty()) {
                            CustomerDto cust = customers.get(random.nextInt(customers.size()));
                            order.setCustomerId(cust.getId());
                        } else {
                            order.setCustomerId(null);
                        }
                    }
                } else {
                    order.setCustomerId(null); // walk-in customer
                }

                // --- Voucher logic: only orders with customerId are eligible ---
                // About 75% of customer orders will get a voucher
                try {
                    if (order.getCustomerId() != null && random.nextDouble() < 0.75) {
                        // [SIMULATED] Use the simulated date
                        LocalDateTime simulateStart = date.atStartOfDay();
                        LocalDateTime simulateEnd = date.atTime(23, 59, 59);

                        List<VoucherDto> valid = voucherService.getAll(List.of(new SearchCriteria("validTo", SearchOperator.GREATER_THAN_OR_EQUAL, simulateEnd.toString()),
                                new SearchCriteria("validFrom", SearchOperator.LESS_THAN_OR_EQUAL, simulateStart.toString())));
                        if (valid != null && !valid.isEmpty()) {
                            VoucherDto v = valid.get(random.nextInt(valid.size()));
                            order.setVoucherCode(v.getCode());
                            // [SIMULATED] log the voucher chosen and the simulated date for easier debugging
                            System.out.println("[DATA-GEN][ORDER][VOUCHER] SimDate=" + date + " picked voucher=" + v.getCode());
                        } else {
                            order.setVoucherCode(null);
                            // [SIMULATED] log when no voucher available for sim date
                            System.out.println("[DATA-GEN][ORDER][VOUCHER] SimDate=" + date + " no valid voucher found.");
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("[DATA-GEN][ORDER] voucher fetch failed: " + ex.getMessage());
                }

                // tính timestamp cho order (ngẫu nhiên phút/giây trong hour)
                int minute = random.nextInt(60);
                int second = random.nextInt(60);
                LocalDateTime orderDateTime = date.atTime(hour, minute, second);

                // Đồng bộ: tránh 2 thread cùng sửa clock cùng lúc
                synchronized (clock) {
                    // set clock về thời điểm đơn này để SaleOrderServiceImpl dùng LocalDateTime.now(clock)
                    clock.reset(orderDateTime);
                    saleOrderService.create(order, user);
                    // (không cần advance clock tiếp; next reset sẽ set đúng theo orderDateTime tiếp theo)
                }
            }
        }
    }

    //Generate batch for a single store
    private void generateBatchForStore(LocalDate date, int minBatch, int maxBatch, List<UserRoleDto> managerUsersOfStore) throws AppException {
        if (managerUsersOfStore == null || managerUsersOfStore.isEmpty()) return;
        List<SupplierDto> suppliers = supplierService.getAll();
        List<ProductGetDto> products = productService.getAll();
        if (suppliers.isEmpty() || products.isEmpty()) return;

        int totalBatch = minBatch + random.nextInt(maxBatch - minBatch + 1);
        int[] batchPerHour = distributeOrdersByHourPoisson(totalBatch, HOUR_WEIGHTS);
        for (int hour = 0; hour < 24; hour++) {
            for (int i = 0; i < batchPerHour[hour]; i++) {
                UserRoleDto userRole = managerUsersOfStore.get(random.nextInt(managerUsersOfStore.size()));
                UserGetDto user = userRole.toUserGetDto();

                int numItems = 1 + random.nextInt(3);
                List<BatchItemDto> items = new ArrayList<>();
                for (int j = 0; j < numItems; j++) {
                    ProductGetDto product = products.get(random.nextInt(products.size()));
                    BatchItemDto item = new BatchItemDto();
                    item.setProductId(product.getId());
                    // random supplier
                    SupplierDto supplier = suppliers.get(random.nextInt(suppliers.size()));
                    item.setSupplierId(supplier.getId());
                    item.setImportPrice(10000 + random.nextInt(80001));
                    LocalDateTime manuDate = LocalDateTime.now().minusDays(random.nextInt(31));
                    item.setManufactureDate(manuDate);
                    item.setExpiryDate(manuDate.plusDays(180 + random.nextInt(186)));
                    item.setOriginalQty(10 + random.nextInt(50));
                    items.add(item);
                }
                BatchDto batch = new BatchDto();
                batch.setBatchCode("BATCH-" + date + "-" + hour + "-" + random.nextInt(10000));
                batch.setBatchItems(items);

                batchService.create(batch, user);
            }
        }
    }

    // Check stock per store and create batch per store when low
    private void checkAndCreateBatchForLowStockPerStore() throws AppException {
        // group managers by store
        Map<Long, List<UserRoleDto>> managersByStore = groupUsersByRoleByStore(ROLE_MANAGER);
        List<ProductGetDto> products = productService.getAll();
        List<SupplierDto> suppliers = supplierService.getAll();
        if (products.isEmpty() || suppliers.isEmpty()) return;

        // Use the simulator date so all generated timestamps are consistent
        LocalDate simDate = this.currentSimulateDate != null ? this.currentSimulateDate : LocalDate.now(clock);

        for (Map.Entry<Long, List<UserRoleDto>> entry : managersByStore.entrySet()) {
            Long storeId = entry.getKey();
            if (storeId == null || storeId < 0) continue;
            List<UserRoleDto> managers = entry.getValue();
            if (managers.isEmpty()) continue;
            UserGetDto manager = managers.get(random.nextInt(managers.size())).toUserGetDto();

            List<BatchItemDto> items = new ArrayList<>();
            for (ProductGetDto product : products) {
                int remainQty = productService.getRemainQuantity(product.getId(), storeId);
                if (remainQty < 50) {
                    BatchItemDto item = new BatchItemDto();
                    item.setProductId(product.getId());
                    SupplierDto supplier = suppliers.get(random.nextInt(suppliers.size()));
                    item.setSupplierId(supplier.getId());
                    item.setImportPrice(10000 + random.nextInt(80001));
                    // manufacture date: some random time within simulated day, minus up-to-30 days (so manuDate <= simDate)
                    int daysAgo = random.nextInt(31); // 0..30 days ago
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
                // choose a realistic batch creation time during the simulated day (e.g. 6..21h)
                int batchHour = 6 + random.nextInt(16); // 6..21
                int batchMinute = random.nextInt(60);
                int batchSecond = random.nextInt(60);
                LocalDateTime batchCreateTime = simDate.atTime(batchHour, batchMinute, batchSecond);

                batch.setBatchCode("BATCH-" + batchCreateTime.toLocalDate() + "-" + random.nextInt(10000));
                batch.setBatchItems(items);

                // ensure created_at used by batchService comes from our simulated clock
                synchronized (clock) {
                    clock.reset(batchCreateTime);
                    batchService.create(batch, manager);
                }
            }
        }
    }

    //Voucher generation: create monthly vouchers (10 per month) with end date at month end
    private void generateMonthlyVouchers(LocalDate date) {
        UserGetDto sysUser = userGetServiceWithRole.get(2696757125120000L).toUserGetDto();
        int month = date.getMonthValue();
        System.out.println("[DATA-GEN][VOUCHER] Generating vouchers for month=" + month);
        for (int i = 0; i < 5; i++) {
            try {
                VoucherDto dto = new VoucherDto();
                dto.setCode("VOUCHER-" + month + "-" + i + "-" + random.nextInt(10000));
                dto.setDescription("Voucher discount percent " + (i + 1) + "of " + month);
                dto.setDiscountPer(5 + random.nextInt(21)); // 5% - 25%
                dto.setDiscountVal(0);
                dto.setValidFrom(LocalDateTime.now(clock).minusYears(1));
                dto.setValidTo(LocalDateTime.now(clock).plusYears(1));
                dto.setQtyTotal(1000L);
                dto.setQtyRedeemed(0L);
                dto.setAudienceType("NaN");
                voucherService.create(dto, sysUser);
            } catch (Exception ex) {
                System.err.println("[DATA-GEN][VOUCHER] Failed create voucher: " + ex.getMessage());
            }
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
                dto.setAudienceType("NaN");
                voucherService.create(dto, sysUser);
            } catch (Exception ex) {
                System.err.println("[DATA-GEN][VOUCHER] Failed create voucher: " + ex.getMessage());
            }
        }
    }

    private LocalDate currentSimulateDate;
    @PostConstruct
    public void initSimDate() {
        this.currentSimulateDate = LocalDate.now(clock);
    }


    @Scheduled(fixedRate = 30000)
    public void scheduledGenerateSaleOrder() {
        try {
            //Voucher generation if month changed
            if (currentSimulateDate.getMonthValue() != currentVoucherGenMonth) {
                currentVoucherGenMonth = currentSimulateDate.getMonthValue();
                generateMonthlyVouchers(currentSimulateDate);
            }

            // --- Reset clock to simulate start-of-day BEFORE launching any tasks ---
            synchronized (clock) {
                // đặt clock về đầu ngày simulated => tất cả thao tác sau dựa trên ngày này
                clock.reset(currentSimulateDate.atStartOfDay());
            }

            //Chạy check batch đồng bộ (or có thể submit và get() để đảm bảo hoàn thành)
            Future<?> batchFuture = batchExecutor.submit(() -> {
                try {
                    checkAndCreateBatchForLowStockPerStore();
                } catch (AppException e) {
                    // log rõ ràng
                    System.err.println("[DATA-GEN][BATCH] check/create batch failed: " + e.getMessage());
                    e.printStackTrace();
                }
            });

            // nếu muốn chờ batch hoàn tất trước khi tạo order => uncomment
            // batchFuture.get();

            // Lấy staff grouped and submit order jobs
            Map<Long, List<UserRoleDto>> staffByStore = groupUsersByRoleByStore(ROLE_STAFF);
            if (staffByStore.isEmpty()) {
                System.out.println("[DATA-GEN] No staff found by store. Skipping order generation.");
                return;
            }

            // submit tasks and collect futures để đợi hoàn thành trước khi tăng ngày/clock
            List<Future<?>> futures = new ArrayList<>();
            for (Map.Entry<Long, List<UserRoleDto>> entry : staffByStore.entrySet()) {
                final List<UserRoleDto> staffOfStore = entry.getValue();
                futures.add(orderExecutor.submit(() -> {
                    try {
                        System.out.println("[DATA-GEN][ORDER] Start generating orders for storeId=" + entry.getKey());
                        generateSaleOrderForStore(currentSimulateDate, 800, 1000, staffOfStore);
                        System.out.println("[DATA-GEN][ORDER] Finished generating orders for storeId=" + entry.getKey());
                    } catch (AppException e) {
                        System.err.println("[DATA-GEN][ORDER] Exception storeId=" + entry.getKey() + " : " + e.getMessage());
                        e.printStackTrace();
                    } catch (Exception ex) {
                        System.err.println("[DATA-GEN][ORDER] Unexpected error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }));
            }

            // Đợi tất cả store job hoàn thành trước khi nhảy ngày
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("[DATA-GEN][ORDER] interrupted while waiting store jobs: " + ie.getMessage());
                } catch (ExecutionException ee) {
                    System.err.println("[DATA-GEN][ORDER] store job failed: " + ee.getMessage());
                }
            }

            // Sau khi mọi thứ hoàn tất, tăng ngày và cập nhật clock trong khối đồng bộ
            synchronized (clock) {
                currentSimulateDate = currentSimulateDate.plusDays(1);
                clock.plusDays(1);
            }
        } catch (Exception e) {
            System.err.println("[DATA-GEN] scheduledGenerateSaleOrder fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: shutdown hook if app stops
//    public void shutdownExecutor() {
//        executor.shutdown();
//        try {
//            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) executor.shutdownNow();
//        } catch (InterruptedException e) {
//            executor.shutdownNow();
//        }
//    }
}
