package ck4.nvb.rsmanagement.base.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
public abstract class EntityDto<ID extends Comparable<ID> & Serializable> extends Dto {

    @Serial
    private static final long serialVersionUID = 1L;

    private ID id;
}
