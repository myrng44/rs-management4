package ck4.nvb.rsmanagement.core.module.stores.storestock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStock;
import ck4.nvb.rsmanagement.core.module.stores.storestock.domain.StoreStockRepository;
import ck4.nvb.rsmanagement.core.module.stores.storestock.service.dto.StoreStockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("storeStockService")
public class StoreStockServiceImpl extends FullAuditedCrudServiceImpl<StoreStockDto, StoreStock, Long, UserGetDto, Long> {

    protected StoreStockServiceImpl(StoreStockRepository repository) {
        super(repository, StoreStock.class);
    }

    @Override
    public StoreStockRepository getRepository() {
        return (StoreStockRepository) super.getRepository();
    }

    @Override
    public StoreStockDto mapToEntityDto(StoreStock entity) {
        return new ModelMapper().map(entity, StoreStockDto.class);
    }
}
