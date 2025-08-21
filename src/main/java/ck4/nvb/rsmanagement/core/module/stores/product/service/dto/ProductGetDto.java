package ck4.nvb.rsmanagement.core.module.stores.product.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductGetDto extends BaseProductDto {
  private String sku;
  private String name;
  private String description;
  private Integer unitPrice;
  private Long categoryId;
  private Long supplierId;
}
