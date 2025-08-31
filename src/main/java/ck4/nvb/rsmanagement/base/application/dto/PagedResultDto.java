package ck4.nvb.rsmanagement.base.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedResultDto<T extends Dto> extends ListResultDto<T> implements HasTotalCount {
  private long totalRecords = 0;

  public PagedResultDto(long totalRecords, List<T> records) {
    super(records);
    this.totalRecords = totalRecords;
  }
}
