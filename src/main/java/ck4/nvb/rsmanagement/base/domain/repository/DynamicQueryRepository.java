package ck4.nvb.rsmanagement.base.domain.repository;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

public interface DynamicQueryRepository<T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> {
    List<T> findByDynamicFilter(List<SearchCriteria> filters, int offset, int limit, Sort sort);
    long countByDynamicFilter(List<SearchCriteria> filters);
}
