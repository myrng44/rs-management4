package ck4.nvb.rsmanagement.core.web.security.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.io.Serial;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDto extends Dto {

  @Serial private static final long serialVersionUID = 1L;

  // user information
  private Long userId;
  private String username;
  private String fullName;
  private String email;
  private String phone;

  // some current info
  private Long currentStoreId;
  private String currentStoreName;
  private Long currentRoleId;
  private String currentRoleName;

  // all user roles across stores
  private List<UserRoleDto> userRoles;

  // current permissions (from current role)
  private List<String> currentPermissions;

  // session metadata
  private String ipAddress;
  private String deviceSession;
  private String traceId;

  public UserSessionDto(UserRoleDto userRole) {
    this.userId = userRole.getUserId();
    this.username = userRole.getUserName();
    this.fullName = userRole.getFullName();
    this.email = userRole.getEmail();
    this.phone = userRole.getPhone();
    this.currentStoreId = userRole.getStoreId();
    this.currentRoleId = userRole.getRoleId();
    this.currentRoleName = userRole.getRoleName();
    this.currentPermissions = userRole.getPermissions();
  }
}
