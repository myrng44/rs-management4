package ck4.nvb.rsmanagement.base.web.controller.api.response;

import java.io.Serial;

public class APIResponse<T> extends BaseAPIResponse<APIResponseHeader, T> {

  @Serial private static final long serialVersionUID = 1L;

  public APIResponse() {
    super();
  }

  public APIResponse(APIResponseHeader header, T body) {
    super(header, body);
  }
}
