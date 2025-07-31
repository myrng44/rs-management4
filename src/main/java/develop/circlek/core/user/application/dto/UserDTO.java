package develop.circlek.core.user.application.dto;

import develop.circlek.base.application.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Long storeId;
    private LocalDateTime lastLogin;
    private List<String> roles;
    private List<String> permissions;
}