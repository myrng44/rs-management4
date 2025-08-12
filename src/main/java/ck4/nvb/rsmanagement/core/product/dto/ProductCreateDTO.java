package ck4.nvb.rsmanagement.core.product.dto;
import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateDTO extends EntityDto<Long> implements CreateInput<ProductEntity>{
    private String sku;

    private String name;

    private String description;

    private int unitPrice;

    private Long categoryId;

    private Long supplierId;

    @Override
    public ProductEntity mapToEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setSku(sku);
        productEntity.setName(name);
        productEntity.setDescription(description);
        productEntity.setUnitPrice(unitPrice);
        productEntity.setCategoryId(categoryId);
        productEntity.setSupplierId(supplierId);
        return productEntity;
    }
}
