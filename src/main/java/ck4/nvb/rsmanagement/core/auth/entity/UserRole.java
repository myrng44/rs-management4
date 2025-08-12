package ck4.nvb.rsmanagement.core.auth.entity;
import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRole extends FullAuditedSerialIdEntity {
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

//    private LocalDateTime grantedAt; // Ví dụ thông tin phụ
}
