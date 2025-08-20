package ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleReturnGetDto extends EntityDto<Long> {
    private String returnCode;
    private String originalSaleOrderId;
    private Long storeId;
    private Long customerId;
    List<SaleReturnItemGetDto> items;
    private String returnReason;
    private Integer totalReturnAmount;
    private String refundMethod;
    private Boolean isProcessed;
    private LocalDateTime processedAt;
    private Long processedBy;
}
