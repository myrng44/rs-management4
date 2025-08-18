package ck4.nvb.rsmanagement.core.module.order.sale_return.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturn;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.ISaleReturnItemService;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.ISaleReturnService;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/return")
public class SaleReturnController extends AuditedCrudController<SaleReturnDto, SaleReturn, Long, UserGetDto, Long, SaleReturnDto, SaleReturnDto> {

    @Autowired
    private ISaleReturnItemService saleReturnItemService;

    @Autowired
    private ModelMapper modelMapper;

    public SaleReturnController(ISaleReturnService service) {
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
            return modelMapper.map(principal, UserGetDto.class);
        }
        return null;
    }
}
