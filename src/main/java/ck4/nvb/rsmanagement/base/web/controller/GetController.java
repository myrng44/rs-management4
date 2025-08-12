package ck4.nvb.rsmanagement.base.web.controller;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.FilterInput;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.ObjectNotFoundException;
import ck4.nvb.rsmanagement.base.application.service.GetService;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteriaParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

public class GetController<D extends EntityDto<ID>,
        T extends IEntity<ID>,
        ID extends Comparable<ID> & Serializable> {

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
    public PagedResultDto<D> getList(
            @RequestParam(required = false, name = "query") List<String> query,
            @RequestParam(required = false, name = "sort") String sort,
            @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
            @RequestParam(required = false, name = "limit", defaultValue = "20") int limit
    ) {
        if (query != null) {
            List<SearchCriteria> params = SearchCriteriaParser.parse(query);
            if (params.isEmpty()) {
                return getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort));
            }

            return getService().getPage(params, new  PagedAndSortedResultRequestDto(offset, limit, sort));
        }
        return getService().getPage(new PagedAndSortedResultRequestDto(offset, limit, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable ID id) {
        D output = getService().get(id);
        if (output == null) {
            throw new ObjectNotFoundException("Object not found. Invalid ID: " + id);
        }
        return ResponseEntity.ok(output);
    }

    public PagedResultDto<D> getList(FilterInput request) {
        if (request.getPaging() == null) {
            request.setPaging(new PagedAndSortedResultRequestDto());
        }
        return getService().getPage(request.mapToSearchCriteria(), request.getPaging());
    }
}
