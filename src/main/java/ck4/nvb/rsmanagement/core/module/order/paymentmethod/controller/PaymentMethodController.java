package ck4.nvb.rsmanagement.core.module.order.paymentmethod.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethod;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.IPaymentService;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.dto.PaymentMethodDto;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.dto.PaymentMethodGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/payment-method")
public class PaymentMethodController
    extends AuditedAPICrudMethod<
        PaymentMethodGetDto,
        PaymentMethod,
        Long,
        UserGetDto,
        Long,
        PaymentMethodDto,
        PaymentMethodDto> {

  @Autowired private IPaymentService service;

  public PaymentMethodController(IPaymentService service) {
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
      return userGetDto;
    }
    return null;
  }

  @PostMapping
  @Override
  public APIResponse<PaymentMethodGetDto> create(
      Authentication auth, @RequestBody PaymentMethodDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{paymentMethodId}")
  @Override
  public APIResponse<PaymentMethodGetDto> update(
      Authentication auth,
      @PathVariable Long paymentMethodId,
      @RequestBody PaymentMethodDto entity) {
    return super.update(auth, paymentMethodId, entity);
  }

  @DeleteMapping("/{paymentMethodId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long paymentMethodId) {
    return super.delete(auth, paymentMethodId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<PaymentMethodGetDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{paymentMethodId}")
  @Override
  public APIResponse<PaymentMethodGetDto> getById(
      Authentication auth, @PathVariable Long paymentMethodId) {
    return super.getById(auth, paymentMethodId);
  }

  @Override
  public APIListResponse<List<PaymentMethodGetDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }

  @GetMapping("/usage")
  public APIListResponse<List<PaymentMethodGetDto.WithUsageStats>> getUsageStats(
      Authentication auth, @RequestParam int days) {
    UserGetDto user = extractUser(auth);

    List<PaymentMethodGetDto.WithUsageStats> output = service.getUsageStatsOfInterval(days);

    return APIResponseBuilder.successList(
        output, 0, output.size(), output.size(), "get usage stats successfully");
  }
}
