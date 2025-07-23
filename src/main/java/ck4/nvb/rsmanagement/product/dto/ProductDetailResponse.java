package ck4.nvb.rsmanagement.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {
    private Long productId;

    private String productName;

    private String categoryName;

    private String supplierName;
    //    public ProductDetailResponse(String productName, String categoryName, String supplierName) {
//        this.productName = productName;
//        this.categoryName = categoryName;
//        this.supplierName = supplierName;
//    }
}
