package ck4.nvb.rsmanagement.core.module.stores.store.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.stores.store.domain.Store;
import ck4.nvb.rsmanagement.core.module.stores.store.service.StoreCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.store.service.dto.StoreDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${rs.api.main.baseUrl}/stores")
public class StoreController
    extends AuditedAPICrudMethod<StoreDto, Store, Long, UserGetDto, Long, StoreDto, StoreDto> {

  public StoreController(StoreCrudServiceImpl storeCrudService) {
    super(storeCrudService);
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
  public APIResponse<StoreDto> create(Authentication auth, @RequestBody StoreDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{storeId}")
  @Override
  public APIResponse<StoreDto> update(
      Authentication auth, @PathVariable Long storeId, @RequestBody StoreDto entity) {
    return super.update(auth, storeId, entity);
  }

  @DeleteMapping("/{storeId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long storeId) {
    return super.delete(auth, storeId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<StoreDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{storeId}")
  @Override
  public APIResponse<StoreDto> getById(Authentication auth, @PathVariable Long storeId) {
    return super.getById(auth, storeId);
  }

  @Override
  public APIListResponse<List<StoreDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
