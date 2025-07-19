package develop.circlek.repository;

import develop.circlek.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r WHERE u.userName = :userName")
    Optional<UserEntity> findByUserNameWithRoles(@Param("userName") String userName);
}