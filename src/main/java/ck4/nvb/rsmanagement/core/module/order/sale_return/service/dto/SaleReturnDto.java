package ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturn;
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
public class SaleReturnDto extends EntityDto<Long> implements CreateInput<SaleReturn>, UpdateInput<SaleReturn> {
    private String returnCode;
    private String originalSaleOrderId;
    private Long storeId;
    private Long customerId;
    private String returnReason;
    private Integer totalReturnAmount;
    private String refundMethod;
    private Boolean isProcessed;
    private LocalDateTime processedAt;
    private Long processedBy;

    @Override
    public SaleReturn mapToEntity() {
        return new ModelMapper().map(this, SaleReturn.class);
    }

    @Override
    public boolean mapToEntity(SaleReturn entity) {
        return false;
    }
}
