package ck4.nvb.rsmanagement.base.web.controller.api.response;

import java.io.Serializable;

public interface BaseApiResponseHeader extends Serializable {
    int getCode();
    void setCode(int code);
    String getMessage();
    void setMessage(String message);
}