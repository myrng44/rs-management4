package develop.circlek.core.order.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class VoucherDTO extends BaseDTO {
    private String code;
    private String description;
    private Integer discountPercent;
    private Integer discountValue;
    private LocalDate startTime;
    private LocalDate expirationTime;
}