package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.StoreEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends BaseRepository<StoreEntity, Long> {
    List<StoreEntity> findByDeletedFalse();
    List<StoreEntity> findByIsActiveTrueAndDeletedFalse();
    Optional<StoreEntity> findByPhoneAndDeletedFalse(String phone);
    
    @Query("SELECT s FROM StoreEntity s LEFT JOIN FETCH s.storeStocks ss WHERE s.id = :id AND s.deleted = false")
    Optional<StoreEntity> findByIdWithStocks(@Param("id") Long id);
}