package ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OrderDetailGetDto extends EntityDto<Long> {
    private String orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer unitPrice;

    public Integer getTotalPrice() {
        if (quantity == null || unitPrice == null) {
            return null;
        }
        return quantity * unitPrice;
    }
}
