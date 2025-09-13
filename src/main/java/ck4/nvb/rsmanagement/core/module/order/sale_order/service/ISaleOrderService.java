package ck4.nvb.rsmanagement.core.module.order.sale_order.service;

import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderGetFullDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

import java.time.LocalDateTime;

public interface ISaleOrderService
    extends FullAuditedCrudService<SaleOrderGetFullDto, SaleOrder, String, UserGetDto, Long> {
    Long getAStoreRevenueBetween(LocalDateTime from, LocalDateTime to, Long storeId) throws AppException;
    Long getAllStoreRevenueBetween(LocalDateTime from, LocalDateTime to) throws AppException;
}
