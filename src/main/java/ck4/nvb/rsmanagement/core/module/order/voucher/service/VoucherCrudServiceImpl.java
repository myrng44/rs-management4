package ck4.nvb.rsmanagement.core.module.order.voucher.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.VoucherRepository;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.dto.VoucherDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("voucherService")
public class VoucherCrudServiceImpl extends FullAuditedCrudServiceImpl<VoucherDto, Voucher, Long, UserGetDto, Long> {
    protected VoucherCrudServiceImpl(VoucherRepository repository) {
        super(repository, Voucher.class);
    }

    @Override
    public VoucherRepository getRepository() {
        return (VoucherRepository) super.getRepository();
    }

    @Override
    public VoucherDto mapToEntityDto(Voucher entity) {
        return new ModelMapper().map(entity, VoucherDto.class);
    }
}
