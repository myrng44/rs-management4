package ck4.nvb.rsmanagement.core.module.stores.stock.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.Stock;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class StockDto extends EntityDto<Long> implements CreateInput<Stock>, UpdateInput<Stock> {

    private String name;

    private String location;

    @Override
    public Stock mapToEntity() {
        return new ModelMapper().map(this, Stock.class);
    }

    @Override
    public boolean mapToEntity(Stock entity) {
        boolean isModified = false;

        if (!name.equals(entity.getName())) {
            entity.setName(name);
            isModified = true;
        }

        if (!location.equals(entity.getLocation())) {
            entity.setLocation(location);
            isModified = true;
        }

        return isModified;
    }
}
