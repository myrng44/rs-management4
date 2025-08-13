package ck4.nvb.rsmanagement.core.module.order.saleallocation.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.domain.SaleAllocation;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.service.SaleAllocationCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto.SaleAllocationDto;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto.SaleAllocationGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/sale-allocation")
public class SaleAllocationController extends AuditedCrudController<
        SaleAllocationGetDto,
        SaleAllocation,
        Long,
        UserGetDto,
        Long,
        SaleAllocationDto,
        SaleAllocationDto> {

    private final SaleAllocationCrudServiceImpl saleAllocationCrudService;

    public SaleAllocationController(SaleAllocationCrudServiceImpl service) {
        super(service);
        this.saleAllocationCrudService = service;
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

//    @GetMapping("/by-sale-line/{saleLineId}")
//    public List<SaleAllocationGetDto> getBySaleLineId(@PathVariable Long saleLineId, Authentication auth) {
//        UserGetDto user = extractUser(auth);
//        return saleAllocationCrudService.getBySaleLineId(saleLineId);
//    }
}
