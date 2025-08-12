package ck4.nvb.rsmanagement.core.auth.entity;
import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
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
public class Role extends FullAuditedSerialIdEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String desc;
}
