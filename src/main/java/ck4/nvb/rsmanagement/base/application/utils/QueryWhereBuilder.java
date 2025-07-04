package ck4.nvb.rsmanagement.base.application.utils;

import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryWhereBuilder {
    public static class SqlAndParams {
        public final String whereClause;
        public final Map<String, Object> params;
        public SqlAndParams(String whereClause, Map<String, Object> params) {
            this.whereClause = whereClause;
            this.params = params;
        }
    }

    public static SqlAndParams buildWhereClause(List<SearchCriteria> filters, Map<String, List<SearchOperator>> searchableKeys) {
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        int idx = 0;
        for (SearchCriteria c : filters) {
            if (searchableKeys != null && !searchableKeys.isEmpty() && !searchableKeys.containsKey(c.getKey())) {
                continue;
            }
            String param = "param" + idx++;
            switch (c.getOperator()) {
                case EQUALS:
                    where.append(" AND ").append(c.getKey()).append(" = :").append(param);
                    params.put(param, c.getValue());
                    break;
                case GREATER_THAN:
                    where.append(" AND ").append(c.getKey()).append(" > :").append(param);
                    params.put(param, c.getValue());
                    break;
                // ... others cases
            }
        }
        return new SqlAndParams(where.toString(), params);
    }
}
