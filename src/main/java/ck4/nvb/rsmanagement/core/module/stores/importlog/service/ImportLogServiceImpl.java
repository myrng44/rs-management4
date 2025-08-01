package ck4.nvb.rsmanagement.core.module.stores.importlog.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLog;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLogRepository;
import ck4.nvb.rsmanagement.core.module.stores.importlog.service.dto.ImportLogDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("importLogService")
public class ImportLogServiceImpl extends FullAuditedCrudServiceImpl<ImportLogDto, ImportLog, String, UserGetDto, Long> {

    protected ImportLogServiceImpl(ImportLogRepository repository) {
        super(repository, ImportLog.class);
    }

    @Override
    public ImportLogRepository getRepository() {
        return (ImportLogRepository) super.getRepository();
    }

    @Override
    public ImportLogDto mapToEntityDto(ImportLog entity) {
        return new ModelMapper().map(entity, ImportLogDto.class);
    }

    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("fromStock", List.of(SearchOperator.EQUALS));
        keys.put("toStore", List.of(SearchOperator.EQUALS));
        keys.put("startDate", List.of(SearchOperator.EQUALS,
                SearchOperator.GREATER_THAN,
                SearchOperator.LESS_THAN,
                SearchOperator.BETWEEN));
        keys.put("deliveryDate", List.of(SearchOperator.EQUALS,
                SearchOperator.LESS_THAN,
                SearchOperator.GREATER_THAN,
                SearchOperator.BETWEEN));
        keys.put("status", List.of(SearchOperator.EQUALS));
        return keys;
    }

    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("startDate");
        keys.add("deliveryDate");
        return keys;
    }
}
