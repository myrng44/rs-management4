package develop.circlek.core.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalProducts;
    private Long todayOrders;
    private Long monthlyRevenue;
    private Long totalCustomer;
}