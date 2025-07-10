package develop.circlek.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_role")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User_role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String store_id;
    String user_id;
    String role_id;
}