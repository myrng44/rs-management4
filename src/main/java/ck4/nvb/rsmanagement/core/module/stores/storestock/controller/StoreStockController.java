package ck4.nvb.rsmanagement.core.module.stores.storestock.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStock;
import ck4.nvb.rsmanagement.core.module.stores.storestock.service.StoreStockServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.storestock.service.dto.StoreStockDto;
import ck4.nvb.rsmanagement.core.module.stores.storestock.service.dto.StoreStockFilterInputDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/store-stock")
public class StoreStockController extends AuditedCrudController<StoreStockDto, StoreStock, Long, UserGetDto, Long, StoreStockDto, StoreStockDto> {

    public StoreStockController(StoreStockServiceImpl service) {
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

    @PostMapping("/filtered")
    public PagedResultDto<StoreStockDto> getList(Authentication auth, @RequestBody StoreStockFilterInputDto request) {
        return super.getList(auth, request);
    }
}
