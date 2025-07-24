package ck4.nvb.rsmanagement.core.module.order.voucher.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.VoucherCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.dto.VoucherDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/voucher")
public class VoucherController extends AuditedCrudController<VoucherDto, Voucher, Long, UserGetDto, Long, VoucherDto, VoucherDto> {

    @Autowired
    public VoucherController(VoucherCrudServiceImpl voucherCrudService) {
        super(voucherCrudService);
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
