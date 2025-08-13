package ck4.nvb.rsmanagement.core.module.stores.batchstock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batchstock.domain.BatchStock;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Getter @Setter
public class BatchStockDto extends EntityDto<String> implements CreateInput<BatchStock>, UpdateInput<BatchStock> {

    private Long batchId;

    private Long storeId;

    private Integer quantityTotal;

    private Integer quantityAvailable;

    private Integer quantityReserved;

    private String status;

    @Override
    public BatchStock mapToEntity() {
        return new ModelMapper().map(this, BatchStock.class);
    }

    @Override
    public boolean mapToEntity(BatchStock entity) {
        return false;
    }
}
