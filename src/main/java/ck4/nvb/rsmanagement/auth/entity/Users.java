package ck4.nvb.rsmanagement.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String username;

    @Column(length = 200)
    private String password;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String fullName;

    private boolean isAccountVerified;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String email;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String phone;

    private Long storeId;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "M/d/yyyy H:mm")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "M/d/yyyy H:mm")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "M/d/yyyy H:mm")
    private LocalDateTime lastLogin;


    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
