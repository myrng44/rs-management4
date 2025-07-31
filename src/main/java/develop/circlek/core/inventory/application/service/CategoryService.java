package develop.circlek.core.inventory.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.auth.application.service.AuthService;
import develop.circlek.core.inventory.domain.entity.CategoryEntity;
import develop.circlek.core.inventory.domain.repository.CategoryRepository;
import develop.circlek.core.inventory.application.dto.CategoryDTO;
import develop.circlek.core.inventory.application.dto.request.CreateCategoryRequest;
import develop.circlek.base.application.exception.NotFoundException;
import develop.circlek.base.application.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService extends BaseService<CategoryEntity, CategoryDTO, Long> {
    
    private final CategoryRepository categoryRepository;
    private final AuthService authService;


    @Override
    protected BaseRepository<CategoryEntity, Long> getRepository() {
        return categoryRepository;
    }
    
    @Override
    protected CategoryDTO convertToDTO(CategoryEntity entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .createAt(LocalDateTime.now())
                .createBy(getCurrentUserId())
                .updateAt(entity.getUpdateAt())
                .updateBy(entity.getUpdateBy())
                .name(entity.getName())
                .description(entity.getDescription())
                .deleted(entity.getDeleted())
                .build();
    }
    
    @Override
    protected CategoryEntity convertToEntity(CategoryDTO dto) {
        return CategoryEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .deleted(dto.getDeleted())
                .build();
    }
    
    @Override
    protected void updateEntityFromDTO(CategoryEntity entity, CategoryDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
    
    @Transactional
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        
        CategoryEntity category = CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleted(false)
                .build();
                
        CategoryEntity savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }
    
    public List<CategoryDTO> findActiveCategories() {
        return categoryRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional
    public void softDelete(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }
}
