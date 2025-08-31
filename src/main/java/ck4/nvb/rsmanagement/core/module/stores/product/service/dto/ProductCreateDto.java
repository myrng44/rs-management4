package ck4.nvb.rsmanagement.core.module.stores.product.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateDto extends EntityDto<Long> implements CreateInput<Product> {

  private String sku;
  private String name;
  private String desc;
  private Integer unitPrice;
  private Long categoryId;

  @Override
  public Product mapToEntity() {
    Product product = new Product();
    product.setSku(sku);
    product.setName(name);
    product.setDesc(desc);
    product.setUnitPrice(unitPrice);
    product.setCategoryId(categoryId);
    return product;
  }
}
