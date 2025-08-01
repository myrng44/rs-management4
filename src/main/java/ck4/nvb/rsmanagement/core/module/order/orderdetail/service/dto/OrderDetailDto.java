package ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class OrderDetailDto extends EntityDto<Long> implements CreateInput<OrderDetail>, UpdateInput<OrderDetail> {

    private String orderId;
    private Long productId;
    private Integer quantity;

    @Override
    public OrderDetail mapToEntity() {
        return new ModelMapper().map(this, OrderDetail.class);
    }

    @Override
    public boolean mapToEntity(OrderDetail entity) {
        boolean isModified = false;

        if (entity == null) {
            return false;
        }
        if (!orderId.equals(entity.getOrderId())) {
            entity.setOrderId(orderId);
            isModified = true;
        }
        if (!productId.equals(entity.getProductId())) {
            entity.setProductId(productId);
            isModified = true;
        }
        if (!quantity.equals(entity.getQuantity())) {
            entity.setQuantity(quantity);
            isModified = true;
        }
        return isModified;
    }
}
