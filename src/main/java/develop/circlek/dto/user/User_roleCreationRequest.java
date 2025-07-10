package develop.circlek.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User_roleCreationRequest {
    String store_id;
    String user_id;
    String role_id;
}
