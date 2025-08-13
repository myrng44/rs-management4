package ck4.nvb.rsmanagement.core.module.order.saleorder.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.saleorder.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.saleorder.service.SaleOrderCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.saleorder.service.dto.SaleOrderDto;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.SaleLineCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class SaleOrderController extends AuditedCrudController<SaleOrderDto, SaleOrder, String, UserGetDto, Long, SaleOrderDto, SaleOrderDto> {

    private final SaleLineCrudServiceImpl orderDetailCrudService;

    public SaleOrderController(SaleOrderCrudServiceImpl service, SaleLineCrudServiceImpl orderDetailCrudService) {
        super(service);
        this.orderDetailCrudService = orderDetailCrudService;
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
