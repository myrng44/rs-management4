package ck4.nvb.rsmanagement.core.module.stores.batch.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BatchGetDto extends EntityDto<Long> {
    private String batchCode;
    private String supplierName;
    private Integer importedPrice;
    private Integer originalQty;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiryDate;
}
