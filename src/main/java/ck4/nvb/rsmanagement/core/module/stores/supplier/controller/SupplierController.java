package ck4.nvb.rsmanagement.core.module.stores.supplier.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.stores.supplier.domain.Supplier;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.ISupplierService;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.dto.SupplierDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/suppliers")
public class SupplierController
    extends AuditedAPICrudMethod<
        SupplierDto, Supplier, Long, UserGetDto, Long, SupplierDto, SupplierDto> {

  public SupplierController(ISupplierService supplierService) {
    super(supplierService);
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
  public APIResponse<SupplierDto> create(Authentication auth, @RequestBody SupplierDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{supplierId}")
  @Override
  public APIResponse<SupplierDto> update(
      Authentication auth, @PathVariable Long supplierId, @RequestBody SupplierDto entity) {
    return super.update(auth, supplierId, entity);
  }

  @DeleteMapping("/{supplierId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long supplierId) {
    return super.delete(auth, supplierId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<SupplierDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{supplierId}")
  @Override
  public APIResponse<SupplierDto> getById(Authentication auth, @PathVariable Long supplierId) {
    return super.getById(auth, supplierId);
  }

  @Override
  public APIListResponse<List<SupplierDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
