package ck4.nvb.rsmanagement.core.module.stores.storestock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStock;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class StoreStockDto extends EntityDto<Long> implements CreateInput<StoreStock>, UpdateInput<StoreStock> {

    private Long productId;

    private Long storeId;

    private Integer quantity;

    @Override
    public StoreStock mapToEntity() {
        return new ModelMapper().map(this, StoreStock.class);
    }

    @Override
    public boolean mapToEntity(StoreStock entity) {
        boolean isModified = false;

        if (productId != entity.getProductId()) {
            entity.setProductId(productId);
            isModified = true;
        }

        if (storeId != entity.getStoreId()) {
            entity.setStoreId(storeId);
            isModified = true;
        }

        if (quantity != entity.getQuantity()) {
            entity.setQuantity(quantity);
            isModified = true;
        }

        return isModified;
    }
}
