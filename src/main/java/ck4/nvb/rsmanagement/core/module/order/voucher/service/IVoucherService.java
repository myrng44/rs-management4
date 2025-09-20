package ck4.nvb.rsmanagement.core.module.order.voucher.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudService;
import ck4.nvb.rsmanagement.core.module.order.voucher.domain.Voucher;
import ck4.nvb.rsmanagement.core.module.order.voucher.service.dto.VoucherDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;

public interface IVoucherService extends FullAuditedCrudService<VoucherDto, Voucher, Long, UserGetDto, Long> {
}
