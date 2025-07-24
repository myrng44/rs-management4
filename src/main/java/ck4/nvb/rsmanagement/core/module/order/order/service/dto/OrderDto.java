package ck4.nvb.rsmanagement.core.module.order.order.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.order.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class OrderDto extends EntityDto<String> implements CreateInput<Order>, UpdateInput<Order> {
    private String orderId;
    private Long customerId;
    private Long storeId;
    private Long voucherId;
    private int finalPrice;
    private String note;
    private Long paymentId;

    @Override
    public Order mapToEntity() {
        return new ModelMapper().map(this, Order.class);
    }

    @Override
    public boolean mapToEntity(Order entity) {
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
        if (finalPrice != entity.getFinalPrice()) {
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
