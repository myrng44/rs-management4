package ck4.nvb.rsmanagement.core.module.order.saleorder.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.saleorder.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.saleorder.service.dto.SaleOrderGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.util.List;

public interface ISaleOrderService
    extends FullAuditedCrudService<SaleOrderGetDto, SaleOrder, String, UserGetDto, Long> {
}