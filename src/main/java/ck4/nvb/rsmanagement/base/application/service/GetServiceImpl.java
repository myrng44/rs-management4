package ck4.nvb.rsmanagement.base.application.service;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.application.exception.IllegalPropertyException;
import ck4.nvb.rsmanagement.base.application.utils.PredicateBuilder;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.domain.repository.BaseRepository;
import ck4.nvb.rsmanagement.base.domain.repository.OffsetBasedPageable;
import ck4.nvb.rsmanagement.base.web.error.FieldError;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * class này là 1 class cơ sở cho các service dạng read-only (lấy dữ liệu) trong spring boot, kết hợp với Spring Data JPA + queryDSL
 * @param <D> => DTO dùng để trả client
 * @param <T> => Entity trong JPA
 * @param <ID> => kiểu dữ liệu của khóa chính ví dụ (Long, UUID)
 */

@Getter
@Transactional(readOnly = true)
public abstract class GetServiceImpl<D extends EntityDto<ID>, T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> implements GetService<D, T, ID> {

    /**
     * EntityManager truy vấn thủ công nếu cần
     */
    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> type;

    /**
     * lớp repository chung
     */
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

    /**
     * mặc định chỉ cho phép sort theo ID
     * các service con có the overide để thêm field khác
     * cái này liên quan trực tiếp đến việc sắp xếp - chỉ filed có trong set này mới được sort
     * @return
     */
    public Set<String> getSortableKeys() {
        Set<String> keys = new HashSet<>();
        keys.add("id");
        return keys;
    }

    /**
     * cho phép map tên field từ request sang tên field thực te trong entity
     * @return
     */
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
        return repository.count(mapToPredicate(filter));
    }

    @Override
    public List<D> getAll(List<SearchCriteria> filter) throws AppException {
        return mapToGetListOutputDto(StreamSupport.stream(getRepository().findAll(mapToPredicate(filter)).spliterator(), false).collect(Collectors.toList()));

    }

    @Override
    @Transactional(readOnly = true)
    public PagedResultDto<D> getPage(PagedAndSortedResultRequestDto paging) throws AppException {
        Page<T> result = getRepository().findAll(mapToPageable(paging));
        return new PagedResultDto<>(result.getTotalElements(), mapToGetListOutputDto(result.getContent()));
    }

    @Override
    public PagedResultDto<D> getPage(List<SearchCriteria> filter, PagedAndSortedResultRequestDto paging) throws AppException {
        Page<T> result = getRepository().findAll(mapToPredicate(filter), mapToPageable(paging));
        return new PagedResultDto<>(result.getTotalElements(), mapToGetListOutputDto(result.getContent()));
    }



//    protected Sort buildSort(PagedAndSortedResultRequestDto paging) {
//        if (paging == null || paging.getSort() == null || paging.getSort().isEmpty()) return Sort.unsorted();
//        String[] sorts = paging.getSort().split(",");
//        List<Sort.Order> orders = new ArrayList<>();
//        Set<String> allowed = getSortableKeys();
//        for (String sort : sorts) {
//            String[] parts = sort.trim().split("\\s+");
//            String key = parts[0];
//            if (!allowed.contains(key)) continue;
//            String direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) ? "DESC" : "ASC";
//            orders.add(new Sort.Order(Sort.Direction.fromString(direction), key));
//        }
//        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
//    }

    /**
     * xử lý sort
     * @param paging
     * @return
     * lấy chuỗi sort từ paging.getSort() vd prices desc
     * tách theo dấu phẩy => nhiêu điều kiện sort
     * mỗi điều kiện : lấy tên cột parts[0] và hướng (desc, asc) => map tên cột nếu có trong getReplaceKeyMap(), kiểm tra cột đó có trong getSortableKeys() không => nếu không thì bỏ qua
     * tạo danh sách sort.Order
     * trả về offSetBasedPageable với sort
     */
    private OffsetBasedPageable mapToPageable(PagedAndSortedResultRequestDto paging) {
        if (getSortableKeys() == null || getSortableKeys().isEmpty()
                || paging.getSort() == null || paging.getSort().isEmpty()) {
            return new OffsetBasedPageable(Math.max(0, paging.getOffset()), Math.max(1, paging.getLimit()));
        }

        // process sorting string
        String[] requestSorts = StringUtils.split(paging.getSort(), ",");
        if (requestSorts == null || requestSorts.length == 0) {
            return new OffsetBasedPageable(Math.max(0, paging.getOffset()), Math.max(1, paging.getLimit()));
        }

        List<Sort.Order> sorts = new ArrayList<>();
        for (String requestSort : requestSorts) {
            String[] parts = StringUtils.split(requestSort.replaceAll("\\s+", " ").trim(), " ");

            // Only add allowed sortable columns
            if (parts == null || parts.length == 0) continue;

            String key = getReplaceKeyMap().getOrDefault(parts[0], parts[0]);
            if (!getSortableKeys().contains(key)) continue;

            // Get ASC or DESC direction
            if (parts.length > 1 && parts[1] != null && parts[1].equalsIgnoreCase("desc")) {
                sorts.add(new Sort.Order(Sort.Direction.DESC, key));
            } else sorts.add(new Sort.Order(Sort.Direction.ASC, key));
        }

        if (sorts.isEmpty()) {
            return new OffsetBasedPageable(Math.max(0, paging.getOffset()), Math.max(1, paging.getLimit()));
        }
        return new OffsetBasedPageable(Math.max(0, paging.getOffset()), Math.max(1, paging.getLimit()), Sort.by(sorts));
    }

    public Predicate mapToPredicate(List<SearchCriteria> filter) {
        if (filter == null || filter.isEmpty()) throw new AppException("Null search criteria");

        List<SearchCriteria> criteria = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();
        for (SearchCriteria c : filter) {
            if (!getSearchableKeys().containsKey(c.getKey())) {
                fieldErrors.add(new FieldError(c.getKey(), "Non searchable key"));
                continue;
            }
            if (!getSearchableKeys().get(c.getKey()).contains(c.getOperator())) {
                fieldErrors.add(new FieldError(c.getKey(), "Not supported operator " + c.getOperator().name()));
                continue;
            }

            criteria.add(c);
        }

        if (!fieldErrors.isEmpty()) throw new IllegalPropertyException(fieldErrors);

        return new PredicateBuilder<>(type).and(criteria).replaceKeyMap(getReplaceKeyMap()).build();
    }
}