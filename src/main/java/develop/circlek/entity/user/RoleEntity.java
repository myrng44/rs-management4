package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleEntity {
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.util.SnowflakeIdGenerator")
    @Column(name = "id")
    Long id;

    @Column(name = "name", unique = true)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "createAt")
    LocalDateTime createAt;

    @Column(name = "createBy")
    Long createBy;

    @Column(name = "updateAt")
    LocalDateTime updateAt;

    @Column(name = "updateBy")
    Long updateBy;

    @Builder.Default
    @Column(name = "deleted")
    Boolean deleted = false;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<RolePermissionEntity> rolePermissions;
}
