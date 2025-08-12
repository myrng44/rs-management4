package ck4.nvb.rsmanagement.base.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public abstract class CreationAuditedEntityDto<ID extends Comparable<ID> & Serializable, UID extends Comparable<UID> & Serializable> extends EntityDto<ID> {
    private UID uid;

    private LocalDateTime creationTime;
}
