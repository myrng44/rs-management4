package ck4.nvb.rsmanagement.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("users-userRoles")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference("role-userRoles")
    private Role role;

//    private LocalDateTime grantedAt; // Ví dụ thông tin phụ
}
