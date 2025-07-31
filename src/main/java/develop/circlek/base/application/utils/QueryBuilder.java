package develop.circlek.base.application.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.DateTimePath;
import java.time.LocalDateTime;
import java.util.Collection;

public class QueryBuilder {
    private final BooleanBuilder builder;
    
    public QueryBuilder() {
        this.builder = new BooleanBuilder();
    }
    
    public QueryBuilder eq(StringPath path, String value) {
        if (value != null && !value.trim().isEmpty()) {
            builder.and(path.eq(value));
        }
        return this;
    }
    
    public QueryBuilder eq(NumberPath<Long> path, Long value) {
        if (value != null) {
            builder.and(path.eq(value));
        }
        return this;
    }
    
    public QueryBuilder like(StringPath path, String value) {
        if (value != null && !value.trim().isEmpty()) {
            builder.and(path.containsIgnoreCase(value));
        }
        return this;
    }
    
    public QueryBuilder in(NumberPath<Long> path, Collection<Long> values) {
        if (values != null && !values.isEmpty()) {
            builder.and(path.in(values));
        }
        return this;
    }
    
    public QueryBuilder between(DateTimePath<LocalDateTime> path, LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            builder.and(path.between(start, end));
        }
        return this;
    }
    
    public Predicate build() {
        return builder;
    }
}