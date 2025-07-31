package develop.circlek.core.inventory.application.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportLogDTO {
    private String id;
    private Long fromStockId;
    private String fromStockName;
    private Long toStoreId;
    private String toStoreName;
    private LocalDateTime startDate;
    private LocalDateTime deliveryDate;
    private Boolean status;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Boolean deleted;
    private List<ImportedProductDTO> importedProducts;
}