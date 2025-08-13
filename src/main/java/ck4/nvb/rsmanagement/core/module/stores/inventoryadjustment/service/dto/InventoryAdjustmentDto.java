package ck4.nvb.rsmanagement.core.module.stores.inventoryadjustment.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.inventoryadjustment.domain.InventoryAdjustment;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class InventoryAdjustmentDto extends EntityDto<Long> implements CreateInput<InventoryAdjustment>, UpdateInput<InventoryAdjustment> {

    private Long batchStockId;

    private Integer changeQuantity;

    private String reason;

    @Override
    public InventoryAdjustment mapToEntity() {
        return new ModelMapper().map(this, InventoryAdjustment.class);
    }

    @Override
    public boolean mapToEntity(InventoryAdjustment entity) {
        boolean isModified = false;

        if (batchStockId != entity.getBatchStockId()) {
            entity.setBatchStockId(batchStockId);
            isModified = true;
        }

        if ( changeQuantity!= entity.getChangeQuantity()) {
            entity.setChangeQuantity(changeQuantity);
            isModified = true;
        }

        if (reason != entity.getReason()) {
            entity.setReason(reason);
            isModified = true;
        }

        return isModified;
    }
}
