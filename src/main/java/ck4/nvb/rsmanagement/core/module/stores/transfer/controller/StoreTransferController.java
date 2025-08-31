package ck4.nvb.rsmanagement.core.module.stores.transfer.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.stores.transfer.domain.StoreTransfer;
import ck4.nvb.rsmanagement.core.module.stores.transfer.service.IStoreTransferService;
import ck4.nvb.rsmanagement.core.module.stores.transfer.service.dto.StoreTransferDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/store-transfers")
public class StoreTransferController
    extends AuditedAPICrudMethod<
        StoreTransferDto,
        StoreTransfer,
        Long,
        UserGetDto,
        Long,
        StoreTransferDto,
        StoreTransferDto> {

  public StoreTransferController(IStoreTransferService service) {
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

    if (principal instanceof UserRoleDto userRoleDto) {
      UserGetDto userGetDto = new UserGetDto();
      userGetDto.setId(userRoleDto.getUserId());
      userGetDto.setUserName(userRoleDto.getUserName());
      return userGetDto;
    }
    return null;
  }

  @PostMapping
  @Override
  public APIResponse<StoreTransferDto> create(
      Authentication auth, @RequestBody StoreTransferDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{storeTransferId}")
  @Override
  public APIResponse<StoreTransferDto> update(
      Authentication auth, @PathVariable Long storeTransferId, StoreTransferDto entity) {
    return super.update(auth, storeTransferId, entity);
  }

  @DeleteMapping("/{storeTransferId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long storeTransferId) {
    return super.delete(auth, storeTransferId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<StoreTransferDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{storeTransferId}")
  @Override
  public APIResponse<StoreTransferDto> getById(
      Authentication auth, @PathVariable Long storeTransferId) {
    return super.getById(auth, storeTransferId);
  }

  @Override
  public APIListResponse<List<StoreTransferDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
