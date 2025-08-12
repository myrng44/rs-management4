package ck4.nvb.rsmanagement.base.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public class CreationAuditedGeneratedIdEntity extends GeneratedIdEntity implements CreationAudited<Long> {

    //Fields
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "creator_id")
    private Long creatorId;
}
