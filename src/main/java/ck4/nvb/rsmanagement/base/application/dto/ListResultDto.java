package ck4.nvb.rsmanagement.base.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListResultDto<T extends Dto> extends Dto {

    private final List<T> elements;

    public ListResultDto(List<T> elements) {
        this.elements = elements;
    }
}
