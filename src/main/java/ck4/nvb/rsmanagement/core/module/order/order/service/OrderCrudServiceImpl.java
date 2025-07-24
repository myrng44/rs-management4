package ck4.nvb.rsmanagement.core.module.order.order.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.order.order.domain.Order;
import ck4.nvb.rsmanagement.core.module.order.order.domain.OrderRepository;
import ck4.nvb.rsmanagement.core.module.order.order.service.dto.OrderDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderCrudServiceImpl extends FullAuditedCrudServiceImpl<OrderDto, Order, String, UserGetDto, Long> {

    protected OrderCrudServiceImpl(OrderRepository repository) {
        super(repository, Order.class);
    }

    @Override
    public OrderRepository getRepository() {
        return (OrderRepository) super.getRepository();
    }

    @Override
    public OrderDto mapToEntityDto(Order entity) {
        return null;
    }
}
