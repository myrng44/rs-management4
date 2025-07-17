package ck4.nvb.rsmanagement.core.module.stores.importlog.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLog;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLogRepository;
import ck4.nvb.rsmanagement.core.module.stores.importlog.service.dto.ImportLogDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
