package develop.circlek.core.inventory.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.inventory.domain.entity.CategoryEntity;
import develop.circlek.core.inventory.application.dto.CategoryDTO;
import develop.circlek.core.inventory.application.dto.request.CreateCategoryRequest;
import develop.circlek.core.inventory.application.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController extends BaseController<CategoryEntity, CategoryDTO, Long> {

    private final CategoryService categoryService;

    @Override
    protected BaseService<CategoryEntity, CategoryDTO, Long> getService() {
        return categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        try {
            CategoryDTO category = categoryService.createCategory(request);
            return ResponseEntity.ok(ApiResponse.success(category));
        } catch (Exception e) {
            log.error("Create category failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getActiveCategories() {
        try {
            List<CategoryDTO> categories = categoryService.findActiveCategories();
            return ResponseEntity.ok(ApiResponse.success(categories));
        } catch (Exception e) {
            log.error("Get active categories failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<ApiResponse<String>> softDeleteCategory(@PathVariable Long id) {
        try {
            categoryService.softDelete(id);
            return ResponseEntity.ok(ApiResponse.success("Category soft deleted successfully"));
        } catch (Exception e) {
            log.error("Soft delete category failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}