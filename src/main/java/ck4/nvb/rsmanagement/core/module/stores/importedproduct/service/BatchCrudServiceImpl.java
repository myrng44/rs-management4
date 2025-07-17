package ck4.nvb.rsmanagement.core.module.stores.importedproduct.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.Batch;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.domain.BatchRepository;
import ck4.nvb.rsmanagement.core.module.stores.importedproduct.service.dto.BatchDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("batchService")
public class BatchCrudServiceImpl extends FullAuditedCrudServiceImpl<BatchDto, Batch, Long, UserGetDto, Long> {
    
    protected BatchCrudServiceImpl(BatchRepository repository) {
        super(repository, Batch.class);
    }

    @Override
    public BatchRepository getRepository() {
        return (BatchRepository) super.getRepository();
    }

    @Override
    public BatchDto mapToEntityDto(Batch entity) {
        return new ModelMapper().map(entity, BatchDto.class);
    }
}
