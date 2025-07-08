package ck4.nvb.rsmanagement.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    private String name; // ví dụ: ROLE_ADMIN, ROLE_USER

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonManagedReference("role-userRoles")
    private List<UserRole> userRoles = new ArrayList<>();

    public void addUserRole(UserRole o) {
        userRoles.add(o);
        o.setRole(this);
    }


    public Role(String name) {
        this.name = name;
    }
}
