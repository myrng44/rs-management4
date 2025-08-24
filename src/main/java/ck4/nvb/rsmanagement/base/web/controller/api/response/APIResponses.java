package ck4.nvb.rsmanagement.base.web.controller.api.response;

import java.io.Serial;

public class APIResponses<T> extends BaseAPIResponse<APIResponseHeader, T> {

  @Serial private static final long serialVersionUID = 1L;

  public APIResponses() {
    super();
  }

  public APIResponses(APIResponseHeader header, T body) {
    super(header, body);
  }
}
