package ck4.nvb.rsmanagement.core.module.stores.category.controller;

import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.web.controller.api.method.AuditedAPICrudMethod;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.core.module.stores.category.domain.Category;
import ck4.nvb.rsmanagement.core.module.stores.category.service.ICategoryService;
import ck4.nvb.rsmanagement.core.module.stores.category.service.dto.CategoryDto;
import ck4.nvb.rsmanagement.core.module.users.permission.domain.entity.PermissionCode;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import ck4.nvb.rsmanagement.core.module.users.userrole.service.dto.UserRoleDto;
import ck4.nvb.rsmanagement.core.web.util.RequiredPermission;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rs.api.main.baseUrl}/categories")
public class CategoryController
    extends AuditedAPICrudMethod<
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

  @PostMapping
  @RequiredPermission(PermissionCode.CREATE_CATEGORY)
  @Override
  public APIResponse<CategoryDto> create(Authentication auth, @RequestBody CategoryDto entity) {
    return super.create(auth, entity);
  }

  @PutMapping("/{categoryId}")
  @RequiredPermission(PermissionCode.UPDATE_CATEGORY)
  @Override
  public APIResponse<CategoryDto> update(
      Authentication auth, @PathVariable Long categoryId, @RequestBody CategoryDto entity) {
    return super.update(auth, categoryId, entity);
  }

  @DeleteMapping("/{categoryId}")
  @RequiredPermission(PermissionCode.DELETE_CATEGORY)
  @Override
  public APIResponse<Void> delete(Authentication auth, @PathVariable Long categoryId) {
    return super.delete(auth, categoryId);
  }

  @GetMapping
  @RequiredPermission(PermissionCode.VIEW_CATEGORY)
  @Override
  public APIListResponse<List<CategoryDto>> getList(
      Authentication auth,
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
    return super.getList(auth, query, sort, offset, limit);
  }

  @GetMapping("/{categoryId}")
  @RequiredPermission(PermissionCode.VIEW_CATEGORY)
  @Override
  public APIResponse<CategoryDto> getById(Authentication auth, @PathVariable Long categoryId) {
    return super.getById(auth, categoryId);
  }

  @Override
  public APIListResponse<List<CategoryDto>> getList(Authentication auth, FilterInput request) {
    return super.getList(auth, request);
  }
}
