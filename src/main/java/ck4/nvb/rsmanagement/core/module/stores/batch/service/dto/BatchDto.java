package ck4.nvb.rsmanagement.core.module.stores.batch.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
public class BatchDto extends EntityDto<Long> implements CreateInput<Batch>, UpdateInput<Batch> {
    private Long batchId;
    private Long productId;
    private Integer originalQuantity;
    private Integer importPrice;
    private Date manufacturingDate;
    private Date expiryDate;
    private LocalDate arrivalDate;

    @Override
    public Batch mapToEntity() {
        return new ModelMapper().map(this, Batch.class);
    }

    @Override
    public boolean mapToEntity(Batch entity) {
        return false;
    }
}
