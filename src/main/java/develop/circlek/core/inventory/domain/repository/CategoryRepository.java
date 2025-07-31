package develop.circlek.core.inventory.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.inventory.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByDeletedFalse();
    Optional<CategoryEntity> findByNameAndDeletedFalse(String name);

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.products p WHERE c.id = :id AND c.deleted = false")
    Optional<CategoryEntity> findByIdWithProducts(@Param("id") Long id);
}