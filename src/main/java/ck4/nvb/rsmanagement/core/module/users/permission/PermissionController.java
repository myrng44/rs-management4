package ck4.nvb.rsmanagement.core.module.users.permission;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.Permission;
import ck4.nvb.rsmanagement.core.module.users.permission.service.IPermissionService;
import ck4.nvb.rsmanagement.core.module.users.permission.service.dto.PermissionDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/permissions")
public class PermissionController
    extends AuditedAPICrudMethod<
        PermissionDto, Permission, Long, UserGetDto, Long, PermissionDto, PermissionDto> {

  public PermissionController(IPermissionService service) {
    super(service);
  }

  @Override
  public UserGetDto extractUser(Authentication auth) {
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    Object principal = auth.getPrincipal();
    if (principal instanceof UserGetDto) {
      return (UserGetDto) principal;
    }

    if (principal instanceof UserRoleDto) {
      UserRoleDto userRoleDto = (UserRoleDto) principal;
      UserGetDto userGetDto = new UserGetDto();
      userGetDto.setId(userRoleDto.getUserId());
      userGetDto.setUserName(userRoleDto.getUserName());
      return userGetDto;
    }
    return null;
  }

  @PostMapping
  @Override
  public APIResponse<PermissionDto> create(Authentication auth, @RequestBody PermissionDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{permissionId}")
  @Override
  public APIResponse<PermissionDto> update(
      Authentication auth, @PathVariable Long permissionId, @RequestBody PermissionDto entity) {
    return super.update(auth, permissionId, entity);
  }

  @DeleteMapping("/{permissionId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long permissionId) {
    return super.delete(auth, permissionId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<PermissionDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{permissionId}")
  @Override
  public APIResponse<PermissionDto> getById(Authentication auth, @PathVariable Long permissionId) {
    return super.getById(auth, permissionId);
  }

  @Override
  public APIListResponse<List<PermissionDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
