package develop.circlek.core.inventory.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.inventory.domain.entity.ProductEntity;
import develop.circlek.core.inventory.application.dto.ProductDTO;
import develop.circlek.core.inventory.application.dto.request.CreateProductRequest;
import develop.circlek.core.inventory.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController extends BaseController<ProductEntity, ProductDTO, Long> {

    private final ProductService productService;

    @Override
    protected BaseService<ProductEntity, ProductDTO, Long> getService() {
        return productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDTO product = productService.createProduct(request);
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            log.error("Create product failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductWithDetails(@PathVariable Long id) {
        try {
            ProductDTO product = productService.findByIdWithDetails(id);
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            log.error("Get product with details failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getActiveProducts() {
        try {
            List<ProductDTO> products = productService.findActiveProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Get active products failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            List<ProductDTO> products = productService.findByCategory(categoryId);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Get products by category failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsBySupplier(@PathVariable Long supplierId) {
        try {
            List<ProductDTO> products = productService.findBySupplier(supplierId);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Get products by supplier failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProductsByName(@RequestParam String name) {
        try {
            List<ProductDTO> products = productService.searchByName(name);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            log.error("Search products by name failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<ApiResponse<String>> softDeleteProduct(@PathVariable Long id) {
        try {
            productService.softDelete(id);
            return ResponseEntity.ok(ApiResponse.success("Product soft deleted successfully"));
        } catch (Exception e) {
            log.error("Soft delete product failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}