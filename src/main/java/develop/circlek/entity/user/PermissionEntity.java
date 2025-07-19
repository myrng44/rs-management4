package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionEntity {
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.util.SnowflakeIdGenerator")
    @Column(name = "id")
    Long id;

    @Column(name = "code", unique = true)
    String code;

    @Column(name = "permissionDesc")
    String permissionDesc;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<RolePermissionEntity> rolePermissions;
}