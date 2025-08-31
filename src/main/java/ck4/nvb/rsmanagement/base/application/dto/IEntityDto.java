package ck4.nvb.rsmanagement.base.application.dto;

import ck4.nvb.rsmanagement.base.util.JacksonParser;
import java.io.Serializable;

public interface IEntityDto<ID extends Comparable<ID> & Serializable> {
  ID getId();

  default String toJson() {
    return JacksonParser.getInstance().toJson(this);
  }
}
