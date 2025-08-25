package ck4.nvb.rsmanagement.core.module.stores.category.controller;

import ck4.nvb.rsmanagement.base.web.controller.AuditedCrudController;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.core.module.stores.category.domain.Category;
import ck4.nvb.rsmanagement.core.module.stores.category.service.ICategoryService;
import ck4.nvb.rsmanagement.core.module.stores.category.service.dto.CategoryDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/categories")
public class CategoryController
    extends AuditedCrudController<
        CategoryDto, Category, Long, UserGetDto, Long, CategoryDto, CategoryDto> {

  protected CategoryController(ICategoryService categoryService) {
    super(categoryService);
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

  @GetMapping
  @RequiredPermission(PermissionCode.VIEW_CATEGORY)
  @Override
  public APIListResponse<List<CategoryDto>> getList(Authentication auth, List<String> query, String sort, int offset, int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }
}
