package develop.circlek.core.inventory.application.dto.request;

import develop.circlek.base.application.dto.BaseRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CreateImportLogRequest extends BaseRequest {
    @NotNull(message = "From stock ID is required")
    private Long fromStockId;
    
    @NotNull(message = "To store ID is required")
    private Long toStoreId;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "Delivery date is required")
    private LocalDateTime deliveryDate;
    
    private List<ImportProductRequest> products;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImportProductRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        
        @NotNull(message = "Import price is required")
        @Min(value = 0, message = "Import price must be non-negative")
        private Integer importPrice;
        
        @NotNull(message = "Manufacturing date is required")
        private LocalDateTime manufacturingDate;
        
        @NotNull(message = "Expiry date is required")
        private LocalDateTime expiryDate;
        
        private String status = "ACTIVE";
    }
}