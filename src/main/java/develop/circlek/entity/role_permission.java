package develop.circlek.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "role_permission")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class role_permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String roleId;
    String permissionId;
}
