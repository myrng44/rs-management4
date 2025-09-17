package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventorySummaryDto extends EntityDto<Long> {
  List<BatchStockGetDto> batchStocks;
}
