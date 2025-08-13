package ck4.nvb.rsmanagement.core.module.order.saleallocation.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.domain.SaleAllocation;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.domain.SaleAllocationRepository;
import ck4.nvb.rsmanagement.core.module.order.saleallocation.service.dto.SaleAllocationGetDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.springframework.stereotype.Service;

@Service
public class SaleAllocationCrudServiceImpl
        extends FullAuditedCrudServiceImpl<SaleAllocationGetDto, SaleAllocation, Long, UserGetDto, Long>
        implements SaleAllocationService {

    protected SaleAllocationCrudServiceImpl(SaleAllocationRepository repository) {
        super(repository, SaleAllocation.class);
    }

    @Override
    public SaleAllocationRepository getRepository() {
        return (SaleAllocationRepository) super.getRepository();
    }

    @Override
    public SaleAllocationGetDto mapToEntityDto(SaleAllocation entity) {
        SaleAllocationGetDto dto = new SaleAllocationGetDto();
        dto.setId(entity.getId());
        dto.setSaleLineId(entity.getSaleLineId());
        dto.setBatchStockId(entity.getBatchStockId());
        dto.setQtyAllocated(entity.getQtyAllocated());
        dto.setQtyPicked(entity.getQtyPicked());
        dto.setUnitCostSnap(entity.getUnitCostSnap());
        return dto;
    }
}
