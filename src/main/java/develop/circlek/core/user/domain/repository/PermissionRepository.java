package develop.circlek.core.user.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.user.domain.entity.PermissionEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermissionRepository extends BaseRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByCode(String code);
}