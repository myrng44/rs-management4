package ck4.nvb.rsmanagement.core.module.stores.batchstock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.domain.BatchStock;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BatchStockDto extends EntityDto<Long>
    implements CreateInput<BatchStock>, UpdateInput<BatchStock> {

  private Long batchId;
  private Long storeId;
  private Integer qtyTotal;
  private Integer qtyAvailable;
  private Integer qtyReversed;
  private String status;
  private Integer version;

  /*  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss",
      timezone = "Asia/Ho_Chi_Minh")
  private Date startDate;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss",
      timezone = "Asia/Ho_Chi_Minh")
  private Date deliveryDate;*/

  @Override
  public BatchStock mapToEntity() {
    return new ModelMapper().map(this, BatchStock.class);
  }

  @Override
  public boolean mapToEntity(BatchStock entity) {
    return false;
  }
}
