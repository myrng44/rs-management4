package ck4.nvb.rsmanagement.core.module.stores.product.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.core.module.stores.product.domain.Product;
import ck4.nvb.rsmanagement.core.module.stores.product.service.IProductService;
import ck4.nvb.rsmanagement.core.module.stores.product.service.ProductServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductCreateDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductGetDto;
import ck4.nvb.rsmanagement.core.module.stores.product.service.dto.ProductUpdateDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/products")
@Getter
public class ProductController
    extends AuditedCrudController<
        ProductGetDto, Product, Long, UserGetDto, Long, ProductCreateDto, ProductUpdateDto> {

  @Autowired private IProductService productService;

  public ProductController(IProductService productService) {
    super(productService);
    this.productService = productService;
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

  @GetMapping("/qty")
  public ResponseEntity<Integer> remainQuantity(Authentication auth, @RequestParam long productId) {
    UserGetDto user = extractUser(auth);
    return ResponseEntity.ok(getProductService().getRemainQuantity(productId, user.getStoreId()));
  }
}
