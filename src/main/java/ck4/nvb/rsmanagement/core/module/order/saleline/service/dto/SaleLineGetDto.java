package ck4.nvb.rsmanagement.core.module.order.saleline.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SaleLineGetDto extends EntityDto<Long> {
    private String saleOrderId;
    private Long productId;
    private String productName;
    private Integer quantityOrdered;
    private Integer quantityAllocated;
    private Integer quantityPicked;
    private Integer unitPrice;

    public Integer getTotalPrice() {
        if (quantityOrdered == null || unitPrice == null) {
            return null;
        }
        return quantityOrdered * unitPrice;
    }
}
