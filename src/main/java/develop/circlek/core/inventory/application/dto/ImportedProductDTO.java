package develop.circlek.core.inventory.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ImportedProductDTO extends BaseDTO {
    private String importLogId;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
    private Integer importPrice;
    private LocalDateTime manufacturingDate;
    private LocalDateTime expiryDate;
    private Short initialQuantity;
    private Short currentQuantity;
    private String status;
    private Boolean deleted;
    private Boolean isExpired;
    private Boolean isExpiringSoon;
}