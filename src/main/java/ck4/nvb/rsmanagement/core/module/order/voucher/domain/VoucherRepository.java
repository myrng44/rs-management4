package ck4.nvb.rsmanagement.core.module.order.voucher.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("voucherRepository")
public interface VoucherRepository extends BaseFullAuditedRepository<Voucher, Long, Long> {
}
