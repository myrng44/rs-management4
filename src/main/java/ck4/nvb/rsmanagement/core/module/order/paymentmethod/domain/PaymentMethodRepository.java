package ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain;

import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import org.springframework.stereotype.Repository;

@Repository("paymentMethodRepository")
public interface PaymentMethodRepository
    extends BaseFullAuditedRepository<PaymentMethod, Long, Long> {}
