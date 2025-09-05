package ck4.nvb.rsmanagement.core.module.stores.transfer.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.transfer.domain.StoreTransferItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreTransferItemDto extends EntityDto<Long>
    implements CreateInput<StoreTransferItem>, UpdateInput<StoreTransferItem> {
  private Long transferId;

  private Long batchStockId;

  private Integer qtyRequested;

  private Integer qtyTransferred;

  @Override
  public StoreTransferItem mapToEntity() {
    return new ModelMapper().map(this, StoreTransferItem.class);
  }

  @Override
  public boolean mapToEntity(StoreTransferItem entity) {
    return false;
  }
}
