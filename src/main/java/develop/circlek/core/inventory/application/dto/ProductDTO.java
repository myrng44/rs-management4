package develop.circlek.core.inventory.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProductDTO extends BaseDTO {
    private String name;
    private String sku;
    private String description;
    private Integer unitPrice;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private Boolean deleted;
}