package develop.circlek.dto;


import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME")
    String userName;

    @Size(min = 5, message = "PASSWORD")
    String passWord;

    String employeeId;
    Timestamp lastLogin;
    Timestamp createAt;
    BigInteger createBy;
    Timestamp updateAt;
    BigInteger updateBy;
}
