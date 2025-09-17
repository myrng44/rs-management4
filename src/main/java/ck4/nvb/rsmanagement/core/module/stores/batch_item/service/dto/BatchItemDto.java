package ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItem;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemDto extends EntityDto<Long>
    implements CreateInput<BatchItem>, UpdateInput<BatchItem> {
  private Long batchId;
  private Long productId;
  private Long supplierId;
  private Integer originalQty;
  private Integer remainQty;
  private Integer importPrice;
  private LocalDateTime manufactureDate;
  private LocalDateTime expiryDate;

  @Override
  public BatchItem mapToEntity() {
    return new ModelMapper().map(this, BatchItem.class);
  }

  @Override
  public boolean mapToEntity(BatchItem entity) {
    boolean isModified = false;

    if (entity.getRemainQty() != remainQty) {
      entity.setRemainQty(remainQty);
      isModified = true;
    }
    if (entity.getImportPrice() != importPrice) {
      entity.setImportPrice(importPrice);
      isModified = true;
    }
    return isModified;
  }

  public record WithBatchInfo(
      Long id,
      Long batchId,
      String batchCode,
      String productName,
      String supplierName,
      Integer originalQty,
      Integer remainQty,
      Integer importPrice,
      LocalDateTime manufactureDate,
      LocalDateTime expiryDate) {
    public WithBatchInfo(
        BatchItemDto batchItemDto, String batchCode, String productName, String supplierName) {
      this(
          batchItemDto.getId(),
          batchItemDto.getBatchId(),
          batchCode,
          productName,
          supplierName,
          batchItemDto.getOriginalQty(),
          batchItemDto.getRemainQty(),
          batchItemDto.getImportPrice(),
          batchItemDto.getManufactureDate(),
          batchItemDto.getExpiryDate());
    }
  }
}
