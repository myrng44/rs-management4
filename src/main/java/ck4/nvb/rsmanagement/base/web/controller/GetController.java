package ck4.nvb.rsmanagement.base.web.controller;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.GetService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIListResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponse;
import ck4.nvb.rsmanagement.base.web.controller.api.response.APIResponseBuilder;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteriaParser;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class GetController<
    D extends EntityDto<ID>, T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public Logger getLogger() {
    return logger;
  }

  private final GetService<D, T, ID> service;

  protected GetController(GetService<D, T, ID> service) {
    this.service = service;
  }

  public GetService<D, T, ID> getService() {
    return service;
  }

  @GetMapping
  public ResponseEntity<APIListResponse<List<D>>> getList(
      @RequestParam(required = false, name = "query") List<String> query,
      @RequestParam(required = false, name = "sort") String sort,
      @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
      @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
/*    if (query != null) {
      List<SearchCriteria> params = SearchCriteriaParser.parse(query);
      if (params.isEmpty()) {
        return getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort));
      }

      return getService().getPage(params, new PagedAndSortedResultRequestDto(offset, limit, sort));
    }
    return getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort));*/
    PagedAndSortedResultRequestDto paging = new PagedAndSortedResultRequestDto(offset, limit, sort);
    PagedResultDto<D> page;
    if (query != null) {
      List<SearchCriteria> params = SearchCriteriaParser.parse(query);
      page = params.isEmpty() ? getService().getPage(paging) : getService().getPage(params, paging);
    } else {
      page = getService().getPage(paging);
    }

    APIListResponse<List<D>> resp = APIResponseBuilder.paged(page, offset, limit);
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{id}")
  public APIResponse<D> getById(@PathVariable ID id) {
    D output = getService().get(id);
    if (output == null) {
      throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);
    }
    return APIResponseBuilder.ok(output);
  }

  public APIListResponse<List<D>> getList(FilterInput request) {
    if (request.getPaging() == null) {
      request.setPaging(new PagedAndSortedResultRequestDto());
    }
    PagedResultDto<D> page = getService().getPage(request.mapToSearchCriteria(), request.getPaging());
    return APIResponseBuilder.paged(page, request.getPaging().getOffset(), request.getPaging().getLimit());
  }
}
