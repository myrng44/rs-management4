package ck4.nvb.rsmanagement.auth.entity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long store_id;

    private Long user_id;

    private Long role_id;

//    private LocalDateTime grantedAt; // Ví dụ thông tin phụ
}
