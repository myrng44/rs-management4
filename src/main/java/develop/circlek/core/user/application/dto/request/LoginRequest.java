package develop.circlek.core.user.application.dto.request;

import develop.circlek.base.application.dto.BaseRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends BaseRequest {
    String userName;
    String passWord;
}