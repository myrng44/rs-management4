package ck4.nvb.rsmanagement.base.web.controller.api.response;

import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIListResponseMetadata extends APIResponseMetadata {

  @Serial private static final long serialVersionUID = 1L;

  private long offset = 0L;

  private int limit = 0;

  private long totalRecords = 0L;

  public APIListResponseMetadata() {}

  public APIListResponseMetadata(
      int code, String message, long offset, int limit, long totalRecords) {
    super(code, message);
    this.offset = offset;
    this.limit = limit;
    this.totalRecords = totalRecords;
  }

  public APIListResponseMetadata(
      ErrorCode status, String message, long offset, int limit, long totalRecords) {
    super(status, message);
    this.offset = offset;
    this.limit = limit;
    this.totalRecords = totalRecords;
  }
}
