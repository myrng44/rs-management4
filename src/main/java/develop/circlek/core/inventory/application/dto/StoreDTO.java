package develop.circlek.core.inventory.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class StoreDTO extends BaseDTO {
    private String name;
    private String address;
    private String phone;
    private Boolean isActive;
    private Boolean deleted;
}