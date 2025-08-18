package ck4.nvb.rsmanagement.core.module.order.sale_return.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturn;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface ISaleReturnService extends FullAuditedCrudService<SaleReturnDto, SaleReturn, Long, UserGetDto, Long> {
}
