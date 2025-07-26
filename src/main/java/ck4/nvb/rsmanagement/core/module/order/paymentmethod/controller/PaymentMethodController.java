package ck4.nvb.rsmanagement.core.module.order.paymentmethod.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethod;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.PaymentMethodCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.dto.PaymentMethodDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController extends AuditedCrudController<PaymentMethodDto, PaymentMethod, Long, UserGetDto, Long, PaymentMethodDto, PaymentMethodDto> {

    @Autowired
    public PaymentMethodController(PaymentMethodCrudServiceImpl service) {
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
