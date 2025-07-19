package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rolePermission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionEntity {
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.util.SnowflakeIdGenerator")
    @Column(name = "id")
    Long id;

    @Column(name = "roleId")
    Long roleId;

    @Column(name = "permissionId")
    Long permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionId", insertable = false, updatable = false)
    PermissionEntity permission;
}