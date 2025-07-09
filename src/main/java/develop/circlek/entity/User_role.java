package develop.circlek.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User_role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String storeId;
    String userId;
    String roleId;
}