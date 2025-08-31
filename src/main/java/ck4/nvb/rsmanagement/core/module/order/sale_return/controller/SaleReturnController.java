package ck4.nvb.rsmanagement.core.module.order.sale_return.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturn;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.ISaleReturnItemService;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.ISaleReturnService;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnCreateDto;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnGetDto;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnUpdateDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/return")
public class SaleReturnController
    extends AuditedAPICrudMethod<
        SaleReturnGetDto,
        SaleReturn,
        Long,
        UserGetDto,
        Long,
        SaleReturnCreateDto,
        SaleReturnUpdateDto> {

  @Autowired private ISaleReturnItemService saleReturnItemService;

  @Autowired private ModelMapper modelMapper;

  public SaleReturnController(ISaleReturnService service) {
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
  public APIResponse<SaleReturnGetDto> create(
      Authentication auth, @RequestBody SaleReturnCreateDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{saleReturnId}")
  @Override
  public APIResponse<SaleReturnGetDto> update(
      Authentication auth,
      @PathVariable Long saleReturnId,
      @RequestBody SaleReturnUpdateDto entity) {
    return super.update(auth, saleReturnId, entity);
  }

  @DeleteMapping("/{saleReturnId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long saleReturnId) {
    return super.delete(auth, saleReturnId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<SaleReturnGetDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{saleReturnId}")
  @Override
  public APIResponse<SaleReturnGetDto> getById(
      Authentication auth, @PathVariable Long saleReturnId) {
    return super.getById(auth, saleReturnId);
  }

  @Override
  public APIListResponse<List<SaleReturnGetDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
