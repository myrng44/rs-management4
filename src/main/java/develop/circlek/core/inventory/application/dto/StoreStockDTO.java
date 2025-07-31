package develop.circlek.core.inventory.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StoreStockDTO extends BaseDTO {
    private Long productId;
    private String productName;
    private String productSku;
    private Long storeId;
    private String storeName;
    private Integer quantity;
    private String importId;
    private Integer minQuantity;
    private Boolean deleted;
    private Boolean isLowStock;
}