package ck4.nvb.rsmanagement.core.module.stores.category.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.stores.category.domain.Category;
import ck4.nvb.rsmanagement.core.module.stores.category.service.dto.CategoryDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface ICategoryService extends FullAuditedCrudService<CategoryDto, Category, Long, UserGetDto, Long> {
}
