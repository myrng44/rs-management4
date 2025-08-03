package develop.circlek.core.dashboard.application.service;

import develop.circlek.core.dashboard.application.dto.DashboardSummaryDTO;
import develop.circlek.core.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderService orderService;
    private final JdbcTemplate jdbcTemplate;

    public DashboardSummaryDTO getSummary() {
        Long totalProducts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product", Long.class);
        Long totalCustomers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customer WHERE is_deleted = false", Long.class);
        
        return DashboardSummaryDTO.builder()
                .totalProducts(totalProducts != null ? totalProducts : 0L)
                .todayOrders(orderService.getTodayOrdersCount())
                .monthlyRevenue(orderService.getMonthlyRevenue())
                .totalCustomer(totalCustomers != null ? totalCustomers : 0L)
                .build();
    }
}