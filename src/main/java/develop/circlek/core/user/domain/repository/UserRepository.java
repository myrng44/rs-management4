package develop.circlek.core.user.domain.repository;

import develop.circlek.base.domain.repository.BaseRepository;
import develop.circlek.core.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r WHERE u.userName = :userName")
    Optional<UserEntity> findByUserNameWithRoles(@Param("userName") String userName);
}