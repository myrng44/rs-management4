package develop.circlek.core.order.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.OrderEntity;
import develop.circlek.core.order.domain.entity.CustomerEntity;
import develop.circlek.core.order.domain.repository.OrderRepository;
import develop.circlek.core.order.domain.repository.OrderDetailRepository;
import develop.circlek.core.order.application.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<CustomerEntity> customer = orderRepository.findCustomerById(entity.getCustomerId());
        return OrderDTO.builder()
                .customerId(entity.getCustomerId())
                .customerName(customer.isPresent() ? customer.get().getName() : "Unknown")
                .storeId(entity.getStoreId())
                .voucherId(entity.getVoucherId())
                .finalPrice(entity.getFinalPrice())
                .note(entity.getNote())
                .paymentId(entity.getPaymentId())
                .status(entity.getStatus())
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
        return details.stream().map(detail -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", detail.getId());
            map.put("orderId", detail.getOrderId());
            map.put("productId", detail.getProductId());
            map.put("quantity", detail.getQuantity());
            return map;
        }).toList();
    }
    public List<Object[]> getTopProducts(int days, int limit) {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        return orderDetailRepository.findTopProductsByDays(startDate, limit);
    }



    public List<OrderDTO> getRecentOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size, Sort.by("createAt").descending()))
                .map(this::convertToDTO)
                .getContent();
    }
}

