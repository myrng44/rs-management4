package ck4.nvb.rsmanagement.base.domain.entity;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class CreationAuditedSerialIdEntity extends SerialIdEntity implements CreationAudited<Long> {

  // Fields
  @Column(name = "created_time")
  private LocalDateTime createdTime;

  @Column(name = "creator_id")
  private Long creatorId;
}
