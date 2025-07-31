package develop.circlek.core.inventory.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.inventory.application.dto.SupplierDTO;
import develop.circlek.core.inventory.application.dto.request.CreateStoreRequest;
import develop.circlek.core.inventory.application.dto.request.CreateSupplierRequest;
import develop.circlek.core.inventory.domain.entity.StoreEntity;
import develop.circlek.core.inventory.application.dto.StoreDTO;
import develop.circlek.core.inventory.application.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController extends BaseController<StoreEntity, StoreDTO, Long> {

    private final StoreService storeService;

    @Override
    protected BaseService<StoreEntity, StoreDTO, Long> getService() {
        return storeService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<StoreDTO>> createStore(@Valid @RequestBody CreateStoreRequest request) {
        try {
            StoreDTO store = storeService.createStore(request);
            return ResponseEntity.ok(ApiResponse.success(store));
        } catch (Exception e) {
            log.error("Create category failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<StoreDTO>>> getActiveStores() {
        try {
            List<StoreDTO> stores = storeService.findActiveStores();
            return ResponseEntity.ok(ApiResponse.success(stores));
        } catch (Exception e) {
            log.error("Get active stores failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<ApiResponse<String>> softDeleteStore(@PathVariable Long id) {
        try {
            storeService.softDelete(id);
            return ResponseEntity.ok(ApiResponse.success("Store soft deleted successfully"));
        } catch (Exception e) {
            log.error("Soft delete store failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateStore(@PathVariable Long id) {
        try {
            storeService.deactivateStore(id);
            return ResponseEntity.ok(ApiResponse.success("Store deactivated successfully"));
        } catch (Exception e) {
            log.error("Deactivate store failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<String>> activateStore(@PathVariable Long id) {
        try {
            storeService.activateStore(id);
            return ResponseEntity.ok(ApiResponse.success("Store activated successfully"));
        } catch (Exception e) {
            log.error("Activate store failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}