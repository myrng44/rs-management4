package ck4.nvb.rsmanagement.base.web.controller.api.response;

import java.io.Serial;

public class APIListResponse<T> extends BaseAPIResponse<APIListResponseHeader, T> {

    @Serial
    private static final long serialVersionUID = 1L;

    public APIListResponse() {
        super();
    }

    public APIListResponse(APIListResponseHeader header, T body) {
        super(header, body);
    }
}
