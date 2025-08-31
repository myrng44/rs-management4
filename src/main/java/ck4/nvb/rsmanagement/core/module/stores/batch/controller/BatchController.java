package ck4.nvb.rsmanagement.core.module.stores.batch.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.stores.batch.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.IBatchService;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.dto.BatchGetDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;

import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/batches")
public class BatchController
    extends AuditedAPICrudMethod<BatchGetDto, Batch, Long, UserGetDto, Long, BatchDto, BatchDto> {

  protected BatchController(IBatchService batchCrudService) {
    super(batchCrudService);
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
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  @Override
  public APIResponse<BatchGetDto> create(Authentication auth, @RequestBody BatchDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{batchId}")
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  @Override
  public APIResponse<BatchGetDto> update(
      Authentication auth, @PathVariable Long batchId, @RequestBody BatchDto entity) {
    return super.update(auth, batchId, entity);
  }

  @DeleteMapping("/{batchId}")
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long batchId) {
    return super.delete(auth, batchId);
  }

  @GetMapping
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  @Override
  public APIListResponse<List<BatchGetDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{batchId}")
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  @Override
  public APIResponse<BatchGetDto> getById(Authentication auth, @PathVariable Long batchId) {
    return super.getById(auth, batchId);
  }

  @Override
  public APIListResponse<List<BatchGetDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
