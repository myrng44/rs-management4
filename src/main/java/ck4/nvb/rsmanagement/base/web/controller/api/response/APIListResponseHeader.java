package ck4.nvb.rsmanagement.base.web.controller.api.response;

import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class APIListResponseHeader extends APIResponseHeader {

    @Serial
    private static final long serialVersionUID = 1L;

    private long offset = 0L;

    private int limit = 0;

    private long totalRecords = 0L;

    public APIListResponseHeader() {
    }

    public APIListResponseHeader(int code, String message, long offset, int limit, long totalRecords) {
        super(code, message);
        this.offset = offset;
        this.limit = limit;
        this.totalRecords = totalRecords;
    }

    public APIListResponseHeader(ErrorCode status, String message, long offset, int limit, long totalRecords) {
        super(status, message);
        this.offset = offset;
        this.limit = limit;
        this.totalRecords = totalRecords;
    }
}
