package ck4.nvb.rsmanagement.core.module.order.voucher.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.VoucherServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.dto.VoucherDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/vouchers")
public class VoucherController
    extends AuditedAPICrudMethod<
        VoucherDto, Voucher, Long, UserGetDto, Long, VoucherDto, VoucherDto> {

  @Autowired
  public VoucherController(VoucherServiceImpl voucherCrudService) {
    super(voucherCrudService);
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
  @RequiredPermission(PermissionCode.CREATE_VOUCHER)
  @Override
  public APIResponse<VoucherDto> create(Authentication auth, @RequestBody VoucherDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{voucherId}")
  @RequiredPermission(PermissionCode.UPDATE_VOUCHER)
  @Override
  public APIResponse<VoucherDto> update(
      Authentication auth, @PathVariable Long voucherId, @RequestBody VoucherDto entity) {
    return super.update(auth, voucherId, entity);
  }

  @DeleteMapping("/{voucherId}")
  @RequiredPermission(PermissionCode.DELETE_VOUCHER)
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long voucherId) {
    return super.delete(auth, voucherId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<VoucherDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{voucherId}")
  @Override
  public APIResponse<VoucherDto> getById(Authentication auth, @PathVariable Long voucherId) {
    return super.getById(auth, voucherId);
  }

  @Override
  public APIListResponse<List<VoucherDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
