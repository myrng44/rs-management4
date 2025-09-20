package ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BatchStockDto extends EntityDto<Long>
    implements CreateInput<BatchStock>, UpdateInput<BatchStock> {

  private Long batchId;
  private Long storeId;
  private String status;
  private Integer version;

  @Override
  public BatchStock mapToEntity() {
    return new ModelMapper().map(this, BatchStock.class);
  }

  @Override
  public boolean mapToEntity(BatchStock entity) {
    return false;
  }
}
