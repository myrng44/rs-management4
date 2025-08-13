package ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.domain.SaleAllocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class SaleAllocationDto extends EntityDto<Long> implements CreateInput<SaleAllocation>, UpdateInput<SaleAllocation> {

    private Long saleLineId;
    private Long batchStockId;
    private Integer qtyAllocated;
    private Integer qtyPicked;
    private Integer unitCostSnap;

    @Override
    public SaleAllocation mapToEntity() {
        return new ModelMapper().map(this, SaleAllocation.class);
    }

    @Override
    public boolean mapToEntity(SaleAllocation entity) {
        boolean modified = false;

        if (!saleLineId.equals(entity.getSaleLineId())) {
            entity.setSaleLineId(saleLineId);
            modified = true;
        }
        if (!batchStockId.equals(entity.getBatchStockId())) {
            entity.setBatchStockId(batchStockId);
            modified = true;
        }
        if (!qtyAllocated.equals(entity.getQtyAllocated())) {
            entity.setQtyAllocated(qtyAllocated);
            modified = true;
        }
        if (!qtyPicked.equals(entity.getQtyPicked())) {
            entity.setQtyPicked(qtyPicked);
            modified = true;
        }
        if (!unitCostSnap.equals(entity.getUnitCostSnap())) {
            entity.setUnitCostSnap(unitCostSnap);
            modified = true;
        }

        return modified;
    }
}
