package develop.circlek.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserUpdateRequest {
    String userName;
    String passWord;
    String employeeId;
    Timestamp lastLogin;
    Timestamp createAt;
    BigInteger createBy;
    Timestamp updateAt;
    BigInteger updateBy;
}
