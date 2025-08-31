package ck4.nvb.rsmanagement.base.web.controller.api.method;

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
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

@Getter
public class APIGetMethod<
    D extends EntityDto<ID>, T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final GetService<D, T, ID> service;

  protected APIGetMethod(GetService<D, T, ID> service) {
    this.service = service;
  }

  public ResponseEntity<APIListResponse<List<D>>> getList(
      List<String> query, String sort, int offset, int limit) {
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

  public APIResponse<D> getById(ID id) {
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
    PagedResultDto<D> page =
        getService().getPage(request.mapToSearchCriteria(), request.getPaging());
    return APIResponseBuilder.paged(
        page, request.getPaging().getOffset(), request.getPaging().getLimit());
  }
}
