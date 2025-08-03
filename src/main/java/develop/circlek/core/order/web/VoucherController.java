package develop.circlek.core.order.web;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.web.BaseController;
import develop.circlek.core.order.domain.entity.VoucherEntity;
import develop.circlek.core.order.application.dto.VoucherDTO;
import develop.circlek.core.order.application.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController extends BaseController<VoucherEntity, VoucherDTO, Long> {

    private final VoucherService voucherService;

    @Override
    protected BaseService<VoucherEntity, VoucherDTO, Long> getService() {
        return voucherService;
    }
}