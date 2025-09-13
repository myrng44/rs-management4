package ck4.nvb.rsmanagement.core.module.stores.batch_item.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batch_item.domain.BatchItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemDto extends EntityDto<Long> implements CreateInput<BatchItem>, UpdateInput<BatchItem> {
    private Long batchId;
    private Long productId;
    private Long supplierId;
    private Integer originalQty;
    private Integer importPrice;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiryDate;

    @Override
    public BatchItem mapToEntity() {
        return new ModelMapper().map(this, BatchItem.class);
    }

    @Override
    public boolean mapToEntity(BatchItem entity) {
        return false;
    }
}
