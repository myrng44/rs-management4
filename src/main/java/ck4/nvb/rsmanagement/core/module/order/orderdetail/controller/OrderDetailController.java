package ck4.nvb.rsmanagement.core.module.order.orderdetail.controller;

import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.OrderDetailCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailDto;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders/details")
public class OrderDetailController extends AuditedCrudController<OrderDetailGetDto, OrderDetail, Long, UserGetDto, Long, OrderDetailDto, OrderDetailDto> {

    private final OrderDetailCrudServiceImpl orderDetailCrudService;

    public OrderDetailController(OrderDetailCrudServiceImpl service) {
        super(service);
        this.orderDetailCrudService = service;
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

    @GetMapping("/summary/{orderId}")
    public List<OrderDetailGetDto> getOrderDetails(@PathVariable String orderId, Authentication auth) {
        UserGetDto user = extractUser(auth);
        return orderDetailCrudService.getDetailByOrderId(orderId);
    }
}
