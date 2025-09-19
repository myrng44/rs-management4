package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto.BatchItemDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInventoryDto extends EntityDto<Long> {
  String productName;
  Integer remainStock;
  List<BatchItemDto.WithProductStatusInfo> batchItems;
}
