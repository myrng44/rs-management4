package ck4.nvb.rsmanagement.core.module.stores.supplier.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.stores.supplier.domain.Supplier;
import ck4.nvb.rsmanagement.core.module.stores.supplier.domain.SupplierRepository;
import ck4.nvb.rsmanagement.core.module.stores.supplier.service.dto.SupplierDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("supplierService")
public class SupplierServiceImpl extends FullAuditedCrudServiceImpl<SupplierDto, Supplier, Long, UserGetDto, Long> {

    protected SupplierServiceImpl(SupplierRepository repository) {
        super(repository, Supplier.class);
    }

    @Override
    public SupplierRepository getRepository() {
        return (SupplierRepository) super.getRepository();
    }

    @Override
    public SupplierDto mapToEntityDto(Supplier entity) {
        return new ModelMapper().map(entity, SupplierDto.class);
    }
}
