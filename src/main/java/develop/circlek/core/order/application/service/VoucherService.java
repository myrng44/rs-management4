package develop.circlek.core.order.application.service;

import develop.circlek.base.application.service.BaseService;
import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.VoucherEntity;
import develop.circlek.core.order.domain.repository.VoucherRepository;
import develop.circlek.core.order.application.dto.VoucherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherService extends BaseService<VoucherEntity, VoucherDTO, Long> {

    private final VoucherRepository voucherRepository;

    @Override
    protected BaseRepository<VoucherEntity, Long> getRepository() {
        return voucherRepository;
    }

    @Override
    protected VoucherDTO convertToDTO(VoucherEntity entity) {
        return VoucherDTO.builder()
                .code(entity.getCode())
                .description(entity.getDescription())
                .discountPercent(entity.getDiscountPercent())
                .discountValue(entity.getDiscountValue())
                .startTime(entity.getStartTime())
                .expirationTime(entity.getExpirationTime())
                .build();
    }

    @Override
    protected VoucherEntity convertToEntity(VoucherDTO dto) {
        return VoucherEntity.builder()
                .code(dto.getCode())
                .description(dto.getDescription())
                .discountPercent(dto.getDiscountPercent())
                .discountValue(dto.getDiscountValue())
                .startTime(dto.getStartTime())
                .expirationTime(dto.getExpirationTime())
                .build();
    }

    @Override
    protected void updateEntityFromDTO(VoucherEntity entity, VoucherDTO dto) {
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setDiscountPercent(dto.getDiscountPercent());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setStartTime(dto.getStartTime());
        entity.setExpirationTime(dto.getExpirationTime());
    }
}