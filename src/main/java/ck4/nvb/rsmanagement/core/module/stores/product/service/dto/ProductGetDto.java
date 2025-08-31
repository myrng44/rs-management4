package ck4.nvb.rsmanagement.core.module.stores.product.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductGetDto extends EntityDto<Long> {
  private String sku;
  private String name;
  private String desc;
  private Integer unitPrice;
  private Long categoryId;

  // Nested record cho specific use cases
  public record WithSales(
          Long id,
          String sku,
          String name,
          String desc,
          Integer unitPrice,
          Long categoryId,
          Long totalQuantitySold
  ) {
    // Constructor từ ProductGetDto
    public WithSales(ProductGetDto product, Long totalQuantitySold) {
      this(
              product.getId(),
              product.getSku(),
              product.getName(),
              product.getDesc(),
              product.getUnitPrice(),
              product.getCategoryId(),
              totalQuantitySold
      );
    }
  }

  // Có thể có thêm các record khác cho các use case khác
  public record WithStock(
          Long id,
          String sku,
          String name,
          Integer unitPrice,
          Integer remainingStock
  ) {}
}
