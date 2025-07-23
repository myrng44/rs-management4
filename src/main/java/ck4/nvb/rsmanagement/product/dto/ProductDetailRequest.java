package ck4.nvb.rsmanagement.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailRequest {
    private Long id;

    private String productName;

    private String categoryName;

    private String supplierName;
}
