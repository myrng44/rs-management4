package develop.circlek.core.order.web;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.order.domain.entity.OrderEntity;
import develop.circlek.core.order.application.dto.OrderDTO;
import develop.circlek.core.order.application.service.OrderService;
import develop.circlek.base.application.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController extends BaseController<OrderEntity, OrderDTO, String> {

    private final OrderService orderService;

    @Override
    protected BaseService<OrderEntity, OrderDTO, String> getService() {
        return orderService;
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getOrdersCount() {
        Long count = (long) orderService.findAll().size();
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping("/details/summary/{orderId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOrderDetails(@PathVariable String orderId) {
        List<Map<String, Object>> details = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(ApiResponse.success(details));
    }

    @GetMapping("/most")
    public ResponseEntity<ApiResponse<List<Object[]>>> getTopProducts(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "4") int noProducts) {
        List<Object[]> products = orderService.getTopProducts(days, noProducts);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getRecentOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        try {
            List<OrderDTO> orders = orderService.getRecentOrders(page, size);
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}