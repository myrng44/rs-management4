package ck4.nvb.rsmanagement.core.module.stores.category.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.category.domain.Category;
import ck4.nvb.rsmanagement.core.module.stores.category.domain.CategoryRepository;
import ck4.nvb.rsmanagement.core.module.stores.category.service.dto.CategoryDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("categoryService")
public class CategoryServiceImpl extends FullAuditedCrudServiceImpl<CategoryDto, Category, Long, UserGetDto, Long> {

    private CategoryRepository categoryRepository;

    protected CategoryServiceImpl(CategoryRepository repository) {
        super(repository, Category.class);
    }

    @Override
    public CategoryDto mapToEntityDto(Category entity) {
        return new ModelMapper().map(entity, CategoryDto.class);
    }
}
