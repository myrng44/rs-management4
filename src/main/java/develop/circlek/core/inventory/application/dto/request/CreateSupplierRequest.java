package develop.circlek.core.inventory.application.dto.request;

import develop.circlek.base.application.dto.BaseRequest;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CreateSupplierRequest extends BaseRequest {
    private String name;

    private String address;

    private String contact;
}
