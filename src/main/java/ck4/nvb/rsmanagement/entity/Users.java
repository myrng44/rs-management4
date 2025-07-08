package ck4.nvb.rsmanagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("users-userRoles")
    private List<UserRole> userRoles = new ArrayList<>();

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users() {

    }

    public void addUserRole(UserRole o) {
        userRoles.add(o);
        o.setUsers(this);
    }
}
