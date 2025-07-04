package ck4.nvb.rsmanagement.base.application.service;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.domain.repository.BaseRepository;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Transactional(readOnly = true)
public abstract class GetServiceImpl<D extends EntityDto<ID>, T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> implements GetService<D, T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> type;

    private final BaseRepository<T, ID> repository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected GetServiceImpl(BaseRepository<T, ID> repository, Class<T> type) {
        this.repository = repository;
        this.type = type;
    }

    public abstract D mapToEntityDto(T entity);

    public D mapToGetOutputDto(T entity) {
        return mapToEntityDto(entity);
    }

    public List<D> mapToGetListOutputDto(List<T> entities) {
        return entities.stream().map(this::mapToEntityDto).collect(Collectors.toList());
    }

    @Override
    public boolean exists(ID id) {
        return getRepository().existsById(id);
    }

    public T getEntity(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    @Override
    public D get(ID id) throws AppException {
        T entity = getEntity(id);
        if (entity == null) {
            return null;
        }
        return mapToGetOutputDto(entity);
    }

    @Override
    public List<D> getAll() throws AppException {
        return mapToGetListOutputDto(getRepository().findAll());
    }

    public Set<String> getSortableKeys() {
        Set<String> keys = new HashSet<>();
        keys.add("id");
        return keys;
    }

    public Map<String, String> getReplaceKeyMap() {
        return new HashMap<>();
    }

    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = new HashMap<>();
        keys.put("id", List.of(SearchOperator.EQUALS));
        return keys;
    }

    @Override
    public long count(List<SearchCriteria> filter) throws AppException {
        return repository.countByDynamicFilter(filter);
    }

    @Override
    public List<D> getAll(List<SearchCriteria> filter) throws AppException {
        List<T> result = getRepository().findByDynamicFilter(filter, 0, Integer.MAX_VALUE, Sort.unsorted());
        return mapToGetListOutputDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResultDto<D> getPage(PagedAndSortedResultRequestDto paging) throws AppException {
        Sort sort = buildSort(paging);
        int offset = paging.getOffset();
        int limit = paging.getLimit();
        List<T> result = repository.findByDynamicFilter(null, offset, limit, sort);
        long total = repository.count();
        return new PagedResultDto<>(total, mapToGetListOutputDto(result));
    }

    @Override
    public PagedResultDto<D> getPage(List<SearchCriteria> filter, PagedAndSortedResultRequestDto paging) throws AppException {
        Sort sort = buildSort(paging);
        int offset = paging.getOffset();
        int limit = paging.getLimit();
        List<T> result = repository.findByDynamicFilter(filter, offset, limit, sort);
        long total = repository.countByDynamicFilter(filter);
        return new PagedResultDto<>(total, mapToGetListOutputDto(result));
    }

    protected Sort buildSort(PagedAndSortedResultRequestDto paging) {
        if (paging == null || paging.getSort() == null || paging.getSort().isEmpty()) return Sort.unsorted();
        String[] sorts = paging.getSort().split(",");
        List<Sort.Order> orders = new ArrayList<>();
        Set<String> allowed = getSortableKeys();
        for (String sort : sorts) {
            String[] parts = sort.trim().split("\\s+");
            String key = parts[0];
            if (!allowed.contains(key)) continue;
            String direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) ? "DESC" : "ASC";
            orders.add(new Sort.Order(Sort.Direction.fromString(direction), key));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}
