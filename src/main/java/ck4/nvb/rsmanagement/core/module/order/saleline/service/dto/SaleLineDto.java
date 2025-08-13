package ck4.nvb.rsmanagement.core.module.order.saleline.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.order.saleline.domain.SaleLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class SaleLineDto extends EntityDto<Long> implements CreateInput<SaleLine>, UpdateInput<SaleLine> {

    private String saleOrderId;
    private Long productId;
    private Integer quantityOrdered;
    private Integer quantityAllocated;
    private Integer quantityPicked;

    @Override
    public SaleLine mapToEntity() {
        return new ModelMapper().map(this, SaleLine.class);
    }

    @Override
    public boolean mapToEntity(SaleLine entity) {
        boolean isModified = false;

        if (entity == null) {
            return false;
        }
        if (!saleOrderId.equals(entity.getSaleOrderId())) {
            entity.setSaleOrderId(saleOrderId);
            isModified = true;
        }
        if (!productId.equals(entity.getProductId())) {
            entity.setProductId(productId);
            isModified = true;
        }
        if (!quantityOrdered.equals(entity.getQuantityOrdered())) {
            entity.setQuantityOrdered(quantityOrdered);
            isModified = true;
        }

        if (!quantityAllocated.equals(entity.getQuantityAllocated())) {
            entity.setQuantityAllocated(quantityAllocated);
            isModified = true;
        }

        if (!quantityPicked.equals(entity.getQuantityPicked())) {
            entity.setQuantityPicked(quantityPicked);
        }
        return isModified;
    }
}
