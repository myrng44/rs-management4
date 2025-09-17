package ck4.nvb.rsmanagement.core.module.order.sale_order.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleLineService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleOrderService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.*;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.role.domain.entity.RoleName;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class SaleOrderController
    extends AuditedAPICrudMethod<
        SaleOrderGetFullDto,
        SaleOrder,
        String,
        UserGetDto,
        Long,
        SaleOrderCreateDto,
        SaleOrderUpdateDto> {

  @Autowired private ISaleLineService saleLineService;
  @Autowired private ISaleOrderService saleOrderService;
  @Autowired private ModelMapper modelMapper;

  public SaleOrderController(ISaleOrderService service) {
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
      userGetDto.setStoreId(userRoleDto.getStoreId());
      userGetDto.setRoleName(userRoleDto.getRoleName());
      return userGetDto;
    }
    return null;
  }

  @GetMapping("/most-products")
  @RequiredPermission(PermissionCode.VIEW_STORE_REPORT)
  public List<ProductGetDto.WithSales> getMostSoldProductsLastDay(
      Authentication auth, @RequestParam int days, @RequestParam int noProducts) {
    UserGetDto user = extractUser(auth);

    if (user.getRoleName().equals(RoleName.SYSADMIN.getName())
        || user.getRoleName().equals(RoleName.ADMIN.getName())) {
      return saleLineService.getMostSoldProductsLastDay(days, noProducts);
    } else {
      return saleLineService.getMostSoldProductsLastDayOfAStore(
          days, noProducts, user.getStoreId());
    }
  }

  @GetMapping("/{storeId}/most-products")
  public List<ProductGetDto.WithSales> getMostSoldProductsLastDay(
      Authentication auth,
      @PathVariable Long storeId,
      @RequestParam int days,
      @RequestParam int noProducts) {
    UserGetDto user = extractUser(auth);

    return saleLineService.getMostSoldProductsLastDayOfAStore(days, noProducts, storeId);
  }

  @PostMapping
  @RequiredPermission(PermissionCode.CREATE_ORDER)
  @Override
  public APIResponse<SaleOrderGetFullDto> create(
      Authentication auth, @RequestBody SaleOrderCreateDto entity) {
    UserGetDto user = extractUser(auth);

    SaleOrderGetFullDto output = getService().create(entity, user);

    return APIResponseBuilder.success(output, "create order success.");
  }

  @PutMapping("/{orderId}")
  @RequiredPermission(PermissionCode.UPDATE_ORDER)
  @Override
  public APIResponse<SaleOrderGetFullDto> update(
      Authentication auth, @PathVariable String orderId, @RequestBody SaleOrderUpdateDto entity) {
    return super.update(auth, orderId, entity);
  }

  @DeleteMapping("/{orderId}")
  @RequiredPermission(PermissionCode.UPDATE_ORDER)
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable String orderId) {
    return super.delete(auth, orderId);
  }

  @GetMapping
  @RequiredPermission(PermissionCode.VIEW_ORDER)
  @Override
  public APIListResponse<List<SaleOrderGetFullDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{orderId}")
  @RequiredPermission(PermissionCode.VIEW_ORDER)
  @Override
  public APIResponse<SaleOrderGetFullDto> getById(
      Authentication auth, @PathVariable String orderId) {
    return super.getById(auth, orderId);
  }

  @Override
  public APIListResponse<List<SaleOrderGetFullDto>> getList(
      Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }

  @GetMapping("/revenue")
  @RequiredPermission(PermissionCode.VIEW_STORE_REPORT)
  public APIResponse<Long> getAStoreRevenue(
      Authentication auth, @RequestParam(required = false, defaultValue = "30") int days) {
    UserGetDto user = extractUser(auth);
    LocalDateTime end = LocalDateTime.now();
    LocalDateTime start = end.minusDays(days);
    if (RoleName.SYSADMIN.getName().equals(user.getRoleName())
        || RoleName.ADMIN.getName().equals(user.getRoleName())) {
      return APIResponseBuilder.ok(saleOrderService.getAllStoreRevenueBetween(start, end));
    }
    return APIResponseBuilder.ok(
        saleOrderService.getAStoreRevenueBetween(start, end, user.getStoreId()));
  }
}
