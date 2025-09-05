package ck4.nvb.rsmanagement.core.module.order.voucher.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("voucherRedemptionRepository")
public interface VoucherRedemptionRepository
    extends BaseFullAuditedRepository<VoucherRedemption, Long, Long> {
    long countByVoucherIdAndCustomerId(Long voucherId, Long customerId);

}
