package ck4.nvb.rsmanagement.core.module.order.saleorder.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
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
public class SaleOrderUpdateDto extends EntityDto<String> implements UpdateInput<SaleOrder> {
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
    public boolean mapToEntity(SaleOrder entity) {
        boolean isModified = false;
        if (entity == null) {
            return false;
        }
        if (!customerId.equals(entity.getCustomerId())) {
            entity.setCustomerId(customerId);
            isModified = true;
        }
        if (!storeId.equals(entity.getStoreId())) {
            entity.setStoreId(storeId);
            isModified = true;
        }
        if (!voucherId.equals(entity.getVoucherId())) {
            entity.setVoucherId(voucherId);
            isModified = true;
        }
        if (!finalPrice.equals(entity.getFinalPrice())) {
            entity.setFinalPrice(finalPrice);
            isModified = true;
        }
        if (!note.equals(entity.getNote())) {
            entity.setNote(note);
            isModified = true;
        }
        if (!paymentId.equals(entity.getPaymentId())) {
            entity.setPaymentId(paymentId);
            isModified = true;
        }

        return isModified;
    }
}