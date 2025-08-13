package ck4.nvb.rsmanagement.core.module.stores.stock.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.Stock;
import ck4.nvb.rsmanagement.core.module.stores.stock.service.IStockService;
import ck4.nvb.rsmanagement.core.module.stores.stock.service.dto.StockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/stock")
public class StockController extends AuditedCrudController<StockDto, Stock, Long, UserGetDto, Long, StockDto, StockDto> {

    public StockController(IStockService stockService) {
        super(stockService);
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
