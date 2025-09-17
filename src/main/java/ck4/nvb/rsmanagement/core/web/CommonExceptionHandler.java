package ck4.nvb.rsmanagement.core.web;

import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseMetadata;
import ck4.nvb.rsmanagement.base.web.controller.api.response.ValidationErrorResponse;
import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import ck4.nvb.rsmanagement.base.web.error.FieldError;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
  private String traceId() {
    String t = MDC.get("traceId");
    return t == null ? "" : t;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest req) {
    log.error("Unhandled exception", ex); // in full stacktrace
    Map<String, Object> body = Map.of("error", ex.getClass().getName(), "message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<APIResponse<Object>> handleNotFound(ObjectNotFoundException ex) {
    APIResponseMetadata metadata = new APIResponseMetadata(ErrorCode.NOT_FOUND, ex.getMessage());
    metadata.setTimestamp(LocalDateTime.now());
    metadata.setTraceId(traceId());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(metadata, null));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<APIResponse<ValidationErrorResponse>> handleValidation(Exception exc) {
    List<FieldError> fieldErrors;
    if (exc instanceof MethodArgumentNotValidException) {
      fieldErrors =
          ((MethodArgumentNotValidException) exc)
              .getBindingResult().getFieldErrors().stream()
                  .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
                  .collect(Collectors.toList());
    } else {
      fieldErrors =
          ((BindException) exc)
              .getBindingResult().getFieldErrors().stream()
                  .map(e -> new FieldError(e.getField(), e.getDefaultMessage()))
                  .collect(Collectors.toList());
    }

    ValidationErrorResponse val = new ValidationErrorResponse(fieldErrors);
    APIResponseMetadata metadata =
        new APIResponseMetadata(ErrorCode.BAD_REQUEST, "Validation failed");
    metadata.setTimestamp(LocalDateTime.now());
    metadata.setTraceId(traceId());
    return ResponseEntity.badRequest().body(new APIResponse<>(metadata, val));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<APIResponse<Object>> handleAccessDenied(AccessDeniedException e) {
    APIResponseMetadata metadata = new APIResponseMetadata(ErrorCode.FORBIDDEN, e.getMessage());
    log.warn("Access denied: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse<>(metadata, null));
  }

  /*  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse<Object>> handleGeneric(Exception ex) {
    APIResponseMetadata metadata =
        new APIResponseMetadata(ErrorCode.INTERNAL_SERVER_ERROR, "Internal server error");
    metadata.setTimestamp(LocalDateTime.now());
    metadata.setTraceId(traceId());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new APIResponse<>(metadata, null));
  }*/
}
