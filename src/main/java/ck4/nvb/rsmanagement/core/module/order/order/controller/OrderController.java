package ck4.nvb.rsmanagement.core.module.order.order.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.order.domain.Orders;
import ck4.nvb.rsmanagement.core.module.order.order.service.IOrderService;
import ck4.nvb.rsmanagement.core.module.order.order.service.dto.OrderDto;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.IOrderDetailService;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class OrderController extends AuditedCrudController<OrderDto, Orders, String, UserGetDto, Long, OrderDto, OrderDto> {

    @Autowired
    private IOrderDetailService orderDetailCrudService;

    public OrderController(IOrderService service) {
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

    @GetMapping("/most")
    public List<ProductGetDto> getMostSoldProductsLastDay(Authentication auth, @RequestParam int days, @RequestParam int noProducts) {
        UserGetDto user = extractUser(auth);

        return orderDetailCrudService.getMostSoldProductsLastDay(days, noProducts);
    }
}
