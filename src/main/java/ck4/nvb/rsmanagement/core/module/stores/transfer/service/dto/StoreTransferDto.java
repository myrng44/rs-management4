package ck4.nvb.rsmanagement.core.module.stores.transfer.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.module.stores.transfer.domain.StoreTransfer;
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
public class StoreTransferDto extends EntityDto<Long> implements CreateInput<StoreTransfer> {
    private Long fromStoreId;
    private Long toStoreId;
    private LocalDateTime transferDate;
    private String status;

    @Override
    public StoreTransfer mapToEntity() {
        return new ModelMapper().map(this, StoreTransfer.class);
    }
}
