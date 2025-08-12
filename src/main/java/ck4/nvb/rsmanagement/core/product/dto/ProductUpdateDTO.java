package ck4.nvb.rsmanagement.core.product.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateDTO extends EntityDto<Long> implements UpdateInput<ProductEntity>{
    private String sku;

    private String name;

    private String description;

    private int unitPrice;

    private Long categoryId;

    private Long supplierId;

    @Override
    public boolean mapToEntity(ProductEntity entity) {
        boolean isModified = false;

        if (sku == null || !sku.equals(entity.getSku())) {
            entity.setSku(sku);
            isModified = true;
        }

        if (name == null || !name.equals(entity.getName())) {
            entity.setName(name);
            isModified = true;
        }
        if ( description == null || !description.equals(entity.getDescription())) {
            entity.setDescription(description);
            isModified = true;
        }

        if (Double.compare(unitPrice, entity.getUnitPrice()) != 0) {
            entity.setUnitPrice(unitPrice);
            isModified = true;
        }

        if (entity.getCategoryId() == null || (long) categoryId != entity.getCategoryId()) {
            entity.setCategoryId(categoryId);
            isModified = true;
        }
        if (entity.getCategoryId() == null || (long) supplierId != entity.getSupplierId()) {
            entity.setSupplierId(supplierId);
            isModified = true;
        }
        return isModified;
    }
}
