package ck4.nvb.rsmanagement.core.module.order.sale_order.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO summary cho SaleOrder - chỉ chứa thông tin cần thiết cho list view
 */
@Getter
@Setter
@NoArgsConstructor
public class SaleOrderGetSummaryDto extends EntityDto<String> {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    private String customerName;

    private Long storeId;

    private Integer finalPrice;

    private String note;
}
