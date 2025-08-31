package ck4.nvb.rsmanagement.core.module.order.customer.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.Customer;
import ck4.nvb.rsmanagement.core.module.order.customer.service.dto.CustomerDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.time.LocalDateTime;

public interface ICustomerService extends FullAuditedCrudService<CustomerDto, Customer, Long, UserGetDto, Long> {

    public int getNumberOfNewCustomersOfInterval(LocalDateTime start, LocalDateTime end);
}
