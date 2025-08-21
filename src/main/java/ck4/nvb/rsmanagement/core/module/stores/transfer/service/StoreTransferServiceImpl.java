package ck4.nvb.rsmanagement.core.module.stores.transfer.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.transfer.domain.StoreTransfer;
import ck4.nvb.rsmanagement.core.module.stores.transfer.domain.StoreTransferRepository;
import ck4.nvb.rsmanagement.core.module.stores.transfer.service.dto.StoreTransferDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("storeTransferService")
public class StoreTransferServiceImpl extends FullAuditedCrudServiceImpl<StoreTransferDto, StoreTransfer, Long, UserGetDto, Long> implements IStoreTransferService {

    protected StoreTransferServiceImpl(StoreTransferRepository storeTransferRepository) {
        super(storeTransferRepository, StoreTransfer.class);
    }

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StoreTransferRepository getRepository() {
        return (StoreTransferRepository) super.getRepository();
    }

    @Override
    public StoreTransferDto mapToEntityDto(StoreTransfer entity) {
        return modelMapper.map(entity, StoreTransferDto.class);
    }
}
