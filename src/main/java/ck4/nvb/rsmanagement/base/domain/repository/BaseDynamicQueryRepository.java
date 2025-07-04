package ck4.nvb.rsmanagement.base.domain.repository;

import ck4.nvb.rsmanagement.base.application.utils.QueryWhereBuilder;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseDynamicQueryRepository<T extends IEntity<ID>, ID extends Comparable<ID> & Serializable> implements DynamicQueryRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;
    private final String tableName;

    protected BaseDynamicQueryRepository(Class<T> entityClass, String tableName) {
        this.entityClass = entityClass;
        this.tableName = tableName;
    }

    @Override
    public List<T> findByDynamicFilter(List<SearchCriteria> filters, int offset, int limit, Sort sort) {
        QueryWhereBuilder.SqlAndParams sqlAndParams = QueryWhereBuilder.buildWhereClause(filters, getSearchableKeys());
        String sql = "SELECT * FROM " + tableName + sqlAndParams.whereClause + buildOrderByClause(sort) + " LIMIT :limit OFFSET :offset";
        Query query = entityManager.createNativeQuery(sql, entityClass);
        sqlAndParams.params.forEach(query::setParameter);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);
        return query.getResultList();
    }

    @Override
    public long countByDynamicFilter(List<SearchCriteria> filters) {
        QueryWhereBuilder.SqlAndParams sqlAndParams = QueryWhereBuilder.buildWhereClause(filters, getSearchableKeys());
        String sql = "SELECT COUNT(*) FROM " + tableName + sqlAndParams.whereClause;
        Query query = entityManager.createNativeQuery(sql);
        sqlAndParams.params.forEach(query::setParameter);
        Object result = query.getSingleResult();
        return ((Number) result).longValue();
    }

    // Cho phép override nếu entity có key filter đặc biệt
    protected Map<String, List<SearchOperator>> getSearchableKeys() {
        return Collections.emptyMap();
    }

    protected String buildOrderByClause(Sort sort) {
        if (sort == null || sort.isUnsorted()) return "";
        StringBuilder sb = new StringBuilder(" ORDER BY ");
        sort.forEach(order -> {
            sb.append(order.getProperty()).append(" ").append(order.getDirection().name()).append(", ");
        });
        sb.setLength(sb.length() - 2); // remove last comma
        return sb.toString();
    }
}
