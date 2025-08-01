package ck4.nvb.rsmanagement.core.module.dashboard;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.CustomerRepository;
import ck4.nvb.rsmanagement.core.module.order.order.domain.OrderRepository;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.ProductRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service("dashboardService")
@RequiredArgsConstructor
@Getter
public class DashboardServiceImpl implements DashboardService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public DashboardSummaryDto getDashboardSummary() throws AppException {
        int totalProducts = getProductRepository().countProductsByDeletedIsFalse();

        int totalOrders = getOrderRepository().countOrdersByCreatedTimeBetween(LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay());
        long monthlyRevenue = Optional.ofNullable(getOrderRepository().sumTotalFinalPriceBetween(LocalDate.now().withDayOfMonth(1).atStartOfDay(),  LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay())).orElse(0L);

        int totalCustomers = getCustomerRepository().countCustomerByDeletedIsFalse();

        return new DashboardSummaryDto(totalProducts, totalOrders, monthlyRevenue, totalCustomers);
    }
}
