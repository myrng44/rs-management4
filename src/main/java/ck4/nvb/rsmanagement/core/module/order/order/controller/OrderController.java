package ck4.nvb.rsmanagement.core.module.order.order.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.order.domain.Order;
import ck4.nvb.rsmanagement.core.module.order.order.service.OrderCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.order.service.dto.OrderDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class OrderController extends AuditedCrudController<OrderDto, Order, String, UserGetDto, Long, OrderDto, OrderDto> {

    @Autowired
    public OrderController(OrderCrudServiceImpl service) {
        super(service);
    }

    @Override
    public UserGetDto extractUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserGetDto) {
            return (UserGetDto) principal;
        }
        if (principal instanceof UserRoleDto) {
            return new ModelMapper().map(principal, UserGetDto.class);
        }
        return null;
    }
}
