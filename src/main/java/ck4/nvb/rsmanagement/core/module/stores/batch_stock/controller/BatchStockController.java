package ck4.nvb.rsmanagement.core.module.stores.batch_stock.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import ck4.nvb.rsmanagement.core.module.stores.batch.service.IBatchService;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.domain.BatchStock;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.BatchStockServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockDto;
import ck4.nvb.rsmanagement.core.module.stores.batch_stock.service.dto.BatchStockGetDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;

import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/batch-stocks")
public class BatchStockController
    extends AuditedAPICrudMethod<
        BatchStockDto, BatchStock, Long, UserGetDto, Long, BatchStockDto, BatchStockDto> {

  public BatchStockController(BatchStockServiceImpl importLogService) {
    super(importLogService);
  }

  @Autowired private BatchStockServiceImpl batchStockService;

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
      userGetDto.setStoreId(userRoleDto.getStoreId());
      return userGetDto;
    }
    return null;
  }

  @PostMapping
  @Override
  public APIResponse<BatchStockDto> create(Authentication auth, @RequestBody BatchStockDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{batchStockId}")
  @Override
  public APIResponse<BatchStockDto> update(
      Authentication auth, @PathVariable Long batchStockId, @RequestBody BatchStockDto entity) {
    return super.update(auth, batchStockId, entity);
  }

  @DeleteMapping("/{batchStockId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long batchStockId) {
    return super.delete(auth, batchStockId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<BatchStockDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{batchStockId}")
  @Override
  public APIResponse<BatchStockDto> getById(Authentication auth, @PathVariable Long batchStockId) {
    return super.getById(auth, batchStockId);
  }

  @Override
  public APIListResponse<List<BatchStockDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }

  @GetMapping("/products/{productId}")
  @RequiredPermission(PermissionCode.MANAGE_STORE_SETTINGS)
  public APIListResponse<List<BatchStockGetDto>> getAllBatchStocksInfoByProduct(Authentication auth, @PathVariable Long productId) {
    UserGetDto userGetDto = extractUser(auth);
    List<BatchStockGetDto> output = batchStockService.getAllBatchByProduct(productId, userGetDto.getStoreId());
    getLogger().debug("GET /batch-stocks/products/{} called by userId={}, storeId={}", productId,
            userGetDto != null ? userGetDto.getId() : null, userGetDto.getStoreId());
    return APIResponseBuilder.successList(output, 0, 10, output.size(), "listed successfully");
  }

}
