package ck4.nvb.rsmanagement.core.module.order.sale_order.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.order.sale_order.domain.SaleOrder;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleOrderService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderCreateDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.ISaleLineService;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderGetDto;
import ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto.SaleOrderUpdateDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/orders")
public class SaleOrderController
    extends AuditedCrudController<SaleOrderGetDto, SaleOrder, String, UserGetDto, Long, SaleOrderCreateDto, SaleOrderUpdateDto> {

  @Autowired
  private ISaleLineService orderDetailCrudService;

  @Autowired
  private ModelMapper modelMapper;

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
      return userGetDto;
    }
    return null;
  }

  @GetMapping("/most")
  public List<ProductGetDto> getMostSoldProductsLastDay(
      Authentication auth, @RequestParam int days, @RequestParam int noProducts) {
    UserGetDto user = extractUser(auth);

    return orderDetailCrudService.getMostSoldProductsLastDay(days, noProducts);
  }
}
