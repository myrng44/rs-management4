package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "role_permission")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role_permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String role_id;
    String permission_id;
}
