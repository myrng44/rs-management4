package ck4.nvb.rsmanagement.core.module.order.customer.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.order.customer.domain.Customer;
import ck4.nvb.rsmanagement.core.module.order.customer.service.CustomerCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.customer.service.dto.CustomerDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/customers")
public class CustomerController
    extends AuditedAPICrudMethod<
        CustomerDto, Customer, Long, UserGetDto, Long, CustomerDto, CustomerDto> {

  @Autowired
  public CustomerController(CustomerCrudServiceImpl customerCrudService) {
    super(customerCrudService);
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
  public APIResponse<CustomerDto> create(Authentication auth, @RequestBody CustomerDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{customerId}")
  @Override
  public APIResponse<CustomerDto> update(
      Authentication auth, @PathVariable Long customerId, @RequestBody CustomerDto entity) {
    return super.update(auth, customerId, entity);
  }

  @DeleteMapping("/{customerId}")
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long customerId) {
    return super.delete(auth, customerId);
  }

  @GetMapping
  @Override
  public APIListResponse<List<CustomerDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{customerId}")
  @Override
  public APIResponse<CustomerDto> getById(Authentication auth, @PathVariable Long customerId) {
    return super.getById(auth, customerId);
  }

  @Override
  public APIListResponse<List<CustomerDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }

  @GetMapping("/count-new")
  @Override
  public APIResponse<Long> count(@RequestParam(required = false) List<String> query) {
    return super.count(query);
  }
}
