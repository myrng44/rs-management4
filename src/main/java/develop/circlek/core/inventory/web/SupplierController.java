package develop.circlek.core.inventory.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.inventory.application.dto.CategoryDTO;
import develop.circlek.core.inventory.application.dto.request.CreateCategoryRequest;
import develop.circlek.core.inventory.application.dto.request.CreateSupplierRequest;
import develop.circlek.core.inventory.domain.entity.SupplierEntity;
import develop.circlek.core.inventory.application.dto.SupplierDTO;
import develop.circlek.core.inventory.application.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierController extends BaseController<SupplierEntity, SupplierDTO, Long> {

    private final SupplierService supplierService;

    @Override
    protected BaseService<SupplierEntity, SupplierDTO, Long> getService() {
        return supplierService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SupplierDTO>> createCategory(@Valid @RequestBody CreateSupplierRequest request) {
        try {
            SupplierDTO supplier = supplierService.createSupplier(request);
            return ResponseEntity.ok(ApiResponse.success(supplier));
        } catch (Exception e) {
            log.error("Create category failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getActiveSuppliers() {
        try {
            List<SupplierDTO> suppliers = supplierService.findActiveSuppliers();
            return ResponseEntity.ok(ApiResponse.success(suppliers));
        } catch (Exception e) {
            log.error("Get active suppliers failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<ApiResponse<String>> softDeleteSupplier(@PathVariable Long id) {
        try {
            supplierService.softDelete(id);
            return ResponseEntity.ok(ApiResponse.success("Supplier soft deleted successfully"));
        } catch (Exception e) {
            log.error("Soft delete supplier failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}