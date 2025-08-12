package ck4.nvb.rsmanagement.core.auth.entity;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends FullAuditedSerialIdEntity {
    @Column(length = 50, nullable = false, unique = true, name = "code")
    private String code;

    @Column(name = "description")
    private String desc;
}
