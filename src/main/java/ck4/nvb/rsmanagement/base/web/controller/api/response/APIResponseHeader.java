package ck4.nvb.rsmanagement.base.web.controller.api.response;

import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class APIResponseHeader implements BaseApiResponseHeader {
    private LocalDateTime timestamp;
    private int code;
    private String message;
    private String traceId;

    public APIResponseHeader() {
    }

    public APIResponseHeader(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIResponseHeader(ErrorCode status, String message) {
        this.code = status.getValue();
        this.message = message;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}