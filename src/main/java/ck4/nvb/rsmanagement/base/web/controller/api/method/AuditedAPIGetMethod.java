package ck4.nvb.rsmanagement.base.web.controller.api.method;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.CreationAuditedCrudService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
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
import org.springframework.security.core.Authentication;

@Getter
public abstract class AuditedAPIGetMethod<
    D extends EntityDto<ID>,
    T extends IEntity<ID> & CreationAudited<UID>,
    ID extends Comparable<ID> & Serializable,
    User extends EntityDto<UID>,
    UID extends Comparable<UID> & Serializable> {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final CreationAuditedCrudService<D, T, ID, User, UID> service;

  protected AuditedAPIGetMethod(CreationAuditedCrudService<D, T, ID, User, UID> service) {
    this.service = service;
  }

  public abstract User extractUser(Authentication auth);

  public APIListResponse<List<D>> getList(
      Authentication auth, List<String> query, String sort, int offset, int limit) {
    User user = extractUser(auth);

    PagedResultDto<D> page;
    if (query != null) {
      List<SearchCriteria> params = SearchCriteriaParser.parse(query);
      if (params.isEmpty()) {
        page = getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort), user);
      } else {
        page =
            getService()
                .getPage(params, new PagedAndSortedResultRequestDto(offset, limit, sort), user);
      }
    } else {
      page = getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort), user);
    }

    return APIResponseBuilder.paged(page, offset, limit);
  }

  public APIResponse<D> getById(Authentication auth, ID id) {
    User user = extractUser(auth);

    D output = getService().get(id, user);

    if (output == null) throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);
    return APIResponseBuilder.ok(output);
  }

  public APIListResponse<List<D>> getList(Authentication auth, FilterInput request) {
    User user = extractUser(auth);
    if (request.getPaging() == null) request.setPaging(new PagedAndSortedResultRequestDto());
    PagedResultDto<D> page =
        getService().getPage(request.mapToSearchCriteria(), request.getPaging(), user);
    return APIResponseBuilder.paged(
        page, request.getPaging().getOffset(), request.getPaging().getLimit());
  }

  public APIResponse<Long> count(List<String> query) {
    if (query != null) {
      List<SearchCriteria> params = SearchCriteriaParser.parse(query);
      return APIResponseBuilder.ok(getService().count(params));
    }
    // Remove this line - let the service handle deleted filter automatically
    return APIResponseBuilder.ok(getService().count(null)); // or empty list
  }
}
