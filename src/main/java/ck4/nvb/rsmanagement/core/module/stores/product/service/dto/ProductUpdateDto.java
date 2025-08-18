package ck4.nvb.rsmanagement.core.module.stores.product.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateDto extends BaseProductDto implements UpdateInput<Product> {

  private String sku;
  private String name;
  private String desc;
  private Integer unitPrice;
  private Long categoryId;

  @Override
  public boolean mapToEntity(Product entity) {
    boolean isModified = false;

    if (sku == null || !sku.equals(entity.getSku())) {
      entity.setSku(sku);
      isModified = true;
    }

    if (name == null || !name.equals(entity.getName())) {
      entity.setName(name);
      isModified = true;
    }
    if (desc == null || !desc.equals(entity.getDesc())) {
      entity.setDesc(desc);
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
    return isModified;
  }
}
