package ck4.nvb.rsmanagement.core.module.stores.stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.Stock;
import ck4.nvb.rsmanagement.core.module.stores.stock.service.dto.StockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IStockService extends FullAuditedCrudService<StockDto, Stock, Long, UserGetDto, Long> {
}
