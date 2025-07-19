package develop.circlek.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Long storeId;
    private LocalDateTime lastLogin;
    private LocalDateTime createAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private List<String> roles;
    private List<String> permissions;
}