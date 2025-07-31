package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.SupplierEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends BaseRepository<SupplierEntity, Long> {
    List<SupplierEntity> findByDeletedFalse();
    Optional<SupplierEntity> findByNameAndDeletedFalse(String name);

    @Query("SELECT s FROM SupplierEntity s LEFT JOIN FETCH s.products p WHERE s.id = :id AND s.deleted = false")
    Optional<SupplierEntity> findByIdWithProducts(@Param("id") Long id);
}