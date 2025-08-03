package develop.circlek.core.order.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class OrderDTO extends BaseDTO {
    private Long customerId;
    private Long storeId;
    private Long voucherId;
    private BigDecimal finalPrice;
    private String note;
    private Long paymentId;
}