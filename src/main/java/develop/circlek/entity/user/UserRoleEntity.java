package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "userRole")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"user", "role"})
public class UserRoleEntity {
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.util.SnowflakeIdGenerator")
    @Column(name = "id")
    Long id;

    @Column(name = "userId")
    Long userId;

    @Column(name = "roleId")
    Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    RoleEntity role;
}