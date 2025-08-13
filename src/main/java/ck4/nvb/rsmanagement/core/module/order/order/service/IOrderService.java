package ck4.nvb.rsmanagement.core.module.order.order.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.order.domain.Orders;
import ck4.nvb.rsmanagement.core.module.order.order.service.dto.OrderDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IOrderService extends FullAuditedCrudService<OrderDto, Orders, String, UserGetDto, Long> {
}
