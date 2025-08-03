package develop.circlek.core.order.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.order.domain.entity.VoucherEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoucherRepository extends BaseRepository<VoucherEntity, Long> {
    List<VoucherEntity> findByIsDeletedFalse();
}