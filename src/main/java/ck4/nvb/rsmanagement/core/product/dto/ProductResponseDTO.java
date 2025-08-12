package ck4.nvb.rsmanagement.core.product.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO extends EntityDto<Long> {
    private String sku;

    private String name;

    private int unitPrice;

    private String description;

    private Long categoryId;

    private Long supplierId;
}
