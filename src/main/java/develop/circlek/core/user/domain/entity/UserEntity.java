package develop.circlek.core.user.domain.entity;

import develop.circlek.base.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "userRoles")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity<Long> {

    @Column(name = "userName", unique = true, nullable = false)
    String userName;

    @Column(name = "passWord", nullable = false)
    String passWord;

    @Column(name = "fullName")
    String fullName;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "storeId")
    Long storeId;

    @Column(name = "lastLogin")
    LocalDateTime lastLogin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserRoleEntity> userRoles;
}