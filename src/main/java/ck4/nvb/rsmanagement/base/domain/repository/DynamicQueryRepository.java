package ck4.nvb.rsmanagement.base.domain.repository;

import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface DynamicQueryRepository<T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> {
    @Query(value = "SELECT 1", nativeQuery = true)
    List<T> findByDynamicFilter(List<SearchCriteria> filters, int offset, int limit, Sort sort);
    
    @Query(value = "SELECT 1", nativeQuery = true)
    long countByDynamicFilter(List<SearchCriteria> filters);
}
