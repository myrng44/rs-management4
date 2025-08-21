package ck4.nvb.rsmanagement.core.module.users.user.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.core.module.users.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto extends BaseUserDto implements CreateInput<User> {
  private String userName;
  private String password;
  private String fullName;
  private String email;
  private String phone;
  private Long storeId;

  @Override
  public User mapToEntity() {
    User user = new User();

    // required fields set
    user.setUsername(userName);
    user.setPassword(password);
    user.setName(fullName);
    user.setEmail(email);
    user.setPhone(phone);
    user.setStoreId(storeId);

    // auto set fields
    user.setDeleted(false);

    return user;
  }
}
