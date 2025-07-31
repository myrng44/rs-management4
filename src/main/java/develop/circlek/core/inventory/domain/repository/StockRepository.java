package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.StockEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<StockEntity, Long> {
    List<StockEntity> findByDeletedFalse();
    Optional<StockEntity> findByNameAndDeletedFalse(String name);
}