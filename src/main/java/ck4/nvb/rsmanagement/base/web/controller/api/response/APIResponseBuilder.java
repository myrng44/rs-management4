package ck4.nvb.rsmanagement.base.web.controller.api.response;

import ck4.nvb.rsmanagement.base.application.dto.Dto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.web.error.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.MDC;

public final class APIResponseBuilder {
  private APIResponseBuilder() {}

  // lấy traceId từ MDC
  private static String traceIdFromMdc() {
    String t = MDC.get("traceId");
    return t == null ? java.util.UUID.randomUUID().toString() : t;
  }

  private static void setCommonHeaderFields(APIResponseMetadata header) {
    header.setTimestamp(LocalDateTime.now());
    header.setTraceId(traceIdFromMdc());
  }

  public static <T> APIResponse<T> ok(T data) {
    return success(data, "OK");
  }

  // success (single)
  public static <T> APIResponse<T> success(T data, String message) {
    APIResponseMetadata header = new APIResponseMetadata(200, message);
    setCommonHeaderFields(header);
    return new APIResponse<>(header, data);
  }

  // success (paged)
  public static <T extends Dto> APIListResponse<List<T>> paged(
      PagedResultDto<T> page, int offset, int limit) {
    APIListResponseMetadata header =
        new APIListResponseMetadata(ErrorCode.OK, "OK", offset, limit, page.getTotalElements());
    setCommonHeaderFields(header);
    return new APIListResponse<>(header, page.getElements());
  }

  // success (list)
  public static <T> APIListResponse<List<T>> successList(
      List<T> data, long offset, int limit, long totalRecords, String message) {
    APIListResponseMetadata header =
        new APIListResponseMetadata(200, message, offset, limit, totalRecords);
    setCommonHeaderFields(header);
    return new APIListResponse<>(header, data);
  }

  // created
  public static <T> APIResponse<T> created(T data, String message) {
    APIResponseMetadata header = new APIResponseMetadata(201, message);
    setCommonHeaderFields(header);
    return new APIResponse<>(header, data);
  }

  // No content
  public static <T> APIResponse<T> noContent(String message) {
    APIResponseMetadata header = new APIResponseMetadata(ErrorCode.NO_CONTENT.getValue(), message);
    setCommonHeaderFields(header);
    return new APIResponse<>(header, null);
  }

  // error
  public static <T> APIResponse<T> error(ErrorCode errorCode, String message) {
    APIResponseMetadata header = new APIResponseMetadata(errorCode.getValue(), message);
    setCommonHeaderFields(header);
    return new APIResponse<>(header, null);
  }
}
