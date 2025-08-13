package ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleAllocationGetDto extends EntityDto<Long> {
    private Long saleLineId;
    private Long batchStockId;
    private Integer qtyAllocated;
    private Integer qtyPicked;
    private Integer unitCostSnap;
}
