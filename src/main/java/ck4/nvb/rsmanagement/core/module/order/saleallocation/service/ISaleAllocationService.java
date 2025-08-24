package ck4.nvb.rsmanagement.core.module.order.saleallocation.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.domain.SaleAllocation;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto.SaleAllocationDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface ISaleAllocationService
    extends FullAuditedCrudService<SaleAllocationDto, SaleAllocation, Long, UserGetDto, Long> {}
