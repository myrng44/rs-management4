package ck4.nvb.rsmanagement.core.module.order.sale_return.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturn;
import ck4.nvb.rsmanagement.core.module.order.sale_return.domain.SaleReturnRepository;
import ck4.nvb.rsmanagement.core.module.order.sale_return.service.dto.SaleReturnDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("saleReturnService")
public class SaleReturnServiceImpl extends FullAuditedCrudServiceImpl<SaleReturnDto, SaleReturn, Long, UserGetDto, Long> implements ISaleReturnService {

    @Autowired
    private ModelMapper modelMapper;

    protected SaleReturnServiceImpl(SaleReturnRepository repository) {
        super(repository, SaleReturn.class);
    }

    @Override
    public SaleReturnRepository getRepository() {
        return (SaleReturnRepository) super.getRepository();
    }

    @Override
    public SaleReturnDto mapToEntityDto(SaleReturn entity) {
        return modelMapper.map(entity, SaleReturnDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("return_code", List.of(SearchOperator.EQUALS));
        return keys;
    }
}
