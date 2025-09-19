package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.ProductInventoryDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IBatchStockService
    extends FullAuditedCrudService<BatchStockDto, BatchStock, Long, UserGetDto, Long> {
  ProductInventoryDto getProductInventory(Long productId, Long storeId);
}
