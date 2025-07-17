package ck4.nvb.rsmanagement.core.module.stores.stock.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.Stock;
import ck4.nvb.rsmanagement.core.module.stores.stock.domain.StockRepository;
import ck4.nvb.rsmanagement.core.module.stores.stock.service.dto.StockDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("stockService")
public class StockServiceImpl extends FullAuditedCrudServiceImpl<StockDto, Stock, Long, UserGetDto, Long> {

    protected StockServiceImpl(StockRepository repository) {
        super(repository, Stock.class);
    }

    @Override
    public StockRepository getRepository() {
        return (StockRepository) super.getRepository();
    }

    @Override
    public StockDto mapToEntityDto(Stock entity) {
        return new ModelMapper().map(entity, StockDto.class);
    }
}
