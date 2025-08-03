package develop.circlek.core.order.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.OrderEntity;
import develop.circlek.core.order.domain.repository.OrderRepository;
import develop.circlek.core.order.domain.repository.OrderDetailRepository;
import develop.circlek.core.order.application.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService extends BaseService<OrderEntity, OrderDTO, String> {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    protected BaseRepository<OrderEntity, String> getRepository() {
        return orderRepository;
    }

    @Override
    protected OrderDTO convertToDTO(OrderEntity entity) {
        return OrderDTO.builder()
                .customerId(entity.getCustomerId())
                .storeId(entity.getStoreId())
                .voucherId(entity.getVoucherId())
                .finalPrice(entity.getFinalPrice())
                .note(entity.getNote())
                .paymentId(entity.getPaymentId())
                .build();
    }

    @Override
    protected OrderEntity convertToEntity(OrderDTO dto) {
        return OrderEntity.builder()
                .customerId(dto.getCustomerId())
                .storeId(dto.getStoreId())
                .voucherId(dto.getVoucherId())
                .finalPrice(dto.getFinalPrice())
                .note(dto.getNote())
                .paymentId(dto.getPaymentId())
                .build();
    }

    @Override
    protected void updateEntityFromDTO(OrderEntity entity, OrderDTO dto) {
        entity.setCustomerId(dto.getCustomerId());
        entity.setStoreId(dto.getStoreId());
        entity.setVoucherId(dto.getVoucherId());
        entity.setNote(dto.getNote());
        entity.setPaymentId(dto.getPaymentId());
    }

    public Long getTodayOrdersCount() {
        return orderRepository.countTodayOrders();
    }

    public Long getMonthlyRevenue() {
        return orderRepository.getMonthlyRevenue();
    }

    public List<Map<String, Object>> getOrderDetails(String orderId) {
        var details = orderDetailRepository.findByOrderId(orderId);
        return details.stream().map(detail -> 
            Map.of(
                "id", detail.getId(),
                "orderId", detail.getOrderId(),
                "productId", detail.getProductId(),
                "quantity", detail.getQuantity()
            )
        ).toList();
    }

    public List<Object[]> getTopProducts(int days, int limit) {
        return orderDetailRepository.findTopProductsByDays(days, limit);
    }
}