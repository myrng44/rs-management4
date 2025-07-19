package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "userRoles")
public class UserEntity {
    @Id
    @GeneratedValue(generator = "snowflakeGenerator")
    @GenericGenerator(name = "snowflakeGenerator", strategy = "develop.circlek.util.SnowflakeIdGenerator")
    @Column(name = "id")
    Long id;

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

    @Column(name = "createAt")
    LocalDateTime createAt;

    @Column(name = "createBy")
    Long createBy;

    @Column(name = "updateAt")
    LocalDateTime updateAt;

    @Column(name = "updateBy")
    Long updateBy;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserRoleEntity> userRoles;
}