package develop.circlek.core.inventory.web;

import develop.circlek.base.application.dto.ApiResponse;
import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.inventory.domain.entity.StoreStockEntity;
import develop.circlek.core.inventory.application.dto.StoreStockDTO;
import develop.circlek.core.inventory.application.service.StoreStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store-stocks")
@RequiredArgsConstructor
@Slf4j
public class StoreStockController extends BaseController<StoreStockEntity, StoreStockDTO, Long> {

    private final StoreStockService storeStockService;

    @Override
    protected BaseService<StoreStockEntity, StoreStockDTO, Long> getService() {
        return storeStockService;
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<StoreStockDTO>>> getStocksByStore(@PathVariable Long storeId) {
        try {
            List<StoreStockDTO> stocks = storeStockService.findByStore(storeId);
            return ResponseEntity.ok(ApiResponse.success(stocks));
        } catch (Exception e) {
            log.error("Get stocks by store failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<StoreStockDTO>>> getStocksByProduct(@PathVariable Long productId) {
        try {
            List<StoreStockDTO> stocks = storeStockService.findByProduct(productId);
            return ResponseEntity.ok(ApiResponse.success(stocks));
        } catch (Exception e) {
            log.error("Get stocks by product failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<StoreStockDTO>>> getLowStockItems() {
        try {
            List<StoreStockDTO> stocks = storeStockService.findLowStockItems();
            return ResponseEntity.ok(ApiResponse.success(stocks));
        } catch (Exception e) {
            log.error("Get low stock items failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(500, "Internal server error"));
        }
    }

    @PutMapping("/store/{storeId}/product/{productId}/quantity")
    public ResponseEntity<ApiResponse<StoreStockDTO>> updateQuantity(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            StoreStockDTO stock = storeStockService.updateQuantity(storeId, productId, quantity);
            return ResponseEntity.ok(ApiResponse.success(stock));
        } catch (Exception e) {
            log.error("Update quantity failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/store/{storeId}/product/{productId}/min-quantity")
    public ResponseEntity<ApiResponse<StoreStockDTO>> updateMinQuantity(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @RequestParam Integer minQuantity) {
        try {
            StoreStockDTO stock = storeStockService.updateMinQuantity(storeId, productId, minQuantity);
            return ResponseEntity.ok(ApiResponse.success(stock));
        } catch (Exception e) {
            log.error("Update min quantity failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}