package ck4.nvb.rsmanagement.core.web;

import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseHeader;
import ck4.nvb.rsmanagement.base.web.controller.api.response.ValidationErrorResponse;
import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import ck4.nvb.rsmanagement.base.web.error.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    private String traceId() {
        String t = MDC.get("traceId");
        return t == null ? "" : t;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<APIResponse<Object>> handleNotFound(ObjectNotFoundException ex) {
        APIResponseHeader header = new APIResponseHeader(ErrorCode.NOT_FOUND, ex.getMessage());
        header.setTimestamp(LocalDateTime.now());
        header.setTraceId(traceId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(header, null));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<APIResponse<ValidationErrorResponse>> handleValidation(Exception exc) {
        List<FieldError> fieldErrors;
        if (exc instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) exc).getBindingResult()
                    .getFieldErrors().stream()
                    .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
                    .collect(Collectors.toList());
        } else {
            fieldErrors = ((BindException) exc).getBindingResult()
                    .getFieldErrors().stream()
                    .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        ValidationErrorResponse val = new ValidationErrorResponse(fieldErrors);
        APIResponseHeader header = new APIResponseHeader(ErrorCode.BAD_REQUEST, "Validation failed");
        header.setTimestamp(LocalDateTime.now());
        header.setTraceId(traceId());
        return ResponseEntity.badRequest().body(new APIResponse<>(header, val));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleGeneric(Exception ex) {
        // log full stack with traceId
        // logger.error("Unhandled", ex);
        APIResponseHeader header = new APIResponseHeader(ErrorCode.INTERNAL_SERVER_ERROR, "Internal server error");
        header.setTimestamp(LocalDateTime.now());
        header.setTraceId(traceId());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(header, null));
    }
}
