package ck4.nvb.rsmanagement.base.web.controller.api.response;

import java.io.Serial;

public class APIResponse<T> extends BaseAPIResponse<APIResponseMetadata, T> {

  @Serial private static final long serialVersionUID = 1L;

  public APIResponse() {
    super();
  }

  public APIResponse(APIResponseMetadata metadata, T body) {
    super(metadata, body);
  }
}
