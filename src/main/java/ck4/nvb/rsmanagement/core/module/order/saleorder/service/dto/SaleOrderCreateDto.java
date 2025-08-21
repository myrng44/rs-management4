package ck4.nvb.rsmanagement.core.module.order.saleorder.service.dto;
import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.core.module.order.saleline.service.dto.SaleLineDto;
import ck4.nvb.rsmanagement.core.module.order.saleorder.domain.SaleOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderCreateDto extends EntityDto<String> implements CreateInput<SaleOrder> {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @NotEmpty
    private List<SaleLineDto> lines;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long voucherId;

    private Integer finalPrice;

    private String note;

    private Long paymentId;

    @Override
    public SaleOrder mapToEntity() {
        return null;
    }
}