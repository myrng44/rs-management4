package ck4.nvb.rsmanagement.core.module.order.saleline.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.saleline.domain.SaleLine;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.SaleLineCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.dto.SaleLineDto;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.dto.SaleLineGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/sale-line")
public class SaleLineController extends AuditedCrudController<SaleLineGetDto, SaleLine, Long, UserGetDto, Long, SaleLineDto, SaleLineDto> {

    private final SaleLineCrudServiceImpl orderDetailCrudService;

    public SaleLineController(SaleLineCrudServiceImpl service) {
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
    public List<SaleLineGetDto> getOrderDetails(@PathVariable String orderId, Authentication auth) {
        UserGetDto user = extractUser(auth);
        return orderDetailCrudService.getDetailByOrderId(orderId);
    }
}
