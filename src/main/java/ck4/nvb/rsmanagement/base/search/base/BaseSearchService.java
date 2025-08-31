package ck4.nvb.rsmanagement.base.search.base;

import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import ck4.nvb.rsmanagement.base.search.dto.SearchResponse;
import ck4.nvb.rsmanagement.base.search.utils.QueryBuilder;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public abstract class BaseSearchService<T extends BaseSearchDocument<ID>, ID> {
    @Autowired
    protected ElasticsearchClient elasticsearchClient;

    @Autowired
    protected QueryBuilder queryBuilder;

    protected final Class<T> documentClass;

    protected final String indexName;

    @SuppressWarnings("unchecked")
    public BaseSearchService() {
        this.documentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.indexName = resolveIndexName();
    }

    /**
     * Default index name (override if needed)
     */
    protected String resolveIndexName() {
        return documentClass.getSimpleName().toLowerCase();
    }

    protected String getIndexName() {
        return this.indexName;
    }

    // === Public APIs ===

    public SearchResponse<T> simpleTextSearch(String keyword, int page, int size) throws IOException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return SearchResponse.empty();
        }
        Query query = queryBuilder.buildSimpleTextQuery(keyword, getSearchFields());
        return executeSearch(query, page, size);
    }

    public SearchResponse<T> advancedSearch(SearchRequest request) throws IOException {
        Query boolQuery = queryBuilder.buildAdvancedQuery(request, getSearchFields());
        return executeSearch(boolQuery, request.getPage(), request.getSize());
    }

    public SearchResponse<T> fuzzySearch(String keyword, int page, int size) throws IOException {
        Query query = queryBuilder.buildFuzzyQuery(keyword, getSearchFields());
        return executeSearch(query, page, size);
    }

    public List<String> getSuggestions(String prefix, String field) throws IOException {
        Query query = queryBuilder.buildPrefixQuery(prefix, field);

        co.elastic.clients.elasticsearch.core.SearchResponse<T> resp = elasticsearchClient.search(
                s -> s.index(getIndexName())
                        .query(query)
                        .size(10),
                documentClass
        );

        return resp.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(doc -> extractFieldValue(doc, field))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public T indexDocument(T document) throws IOException {
        elasticsearchClient.index(i -> i
                .index(getIndexName())
                .id(document.getId().toString())
                .document(document)
        );
        return document;
    }

    public List<T> bulkIndex(List<T> documents) throws IOException {
        List<BulkOperation> ops = documents.stream()
                .map(doc -> BulkOperation.of(b -> b.index(idx -> idx
                        .index(getIndexName())
                        .id(doc.getId().toString())
                        .document(doc)
                )))
                .collect(Collectors.toList());

        BulkResponse resp = elasticsearchClient.bulk(b -> b.operations(ops));

        if (resp.errors()) {
            // Optional: inspect resp.items() and decide retry/partial-failure handling
            throw new RuntimeException("Bulk index errors occurred. Check BulkResponse.items()");
        }

        return documents;
    }

    public void deleteById(ID id) throws IOException {
        elasticsearchClient.delete(d -> d
                .index(getIndexName())
                .id(id.toString())
        );
    }

    // === Abstracts to implement in concrete service ===

    protected abstract String[] getSearchFields();

    protected abstract String extractFieldValue(T document, String field);

    /**
     * Hook để override nếu muốn apply filter tuỳ chỉnh.
     */
    protected BoolQuery applyCustomFilters(BoolQuery query, SearchRequest request) {
        return query;
    }

    // === Private helper ===

    private SearchResponse<T> executeSearch(Query query, int page, int size) throws IOException {
        co.elastic.clients.elasticsearch.core.SearchResponse<T> resp = elasticsearchClient.search(
                s -> s.index(getIndexName())
                        .query(query)
                        .from(Math.max(0, page * size))
                        .size(size),
                documentClass
        );

        List<T> content = resp.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        long total = 0L;
        if (resp.hits() != null && resp.hits().total() != null) {
            total = resp.hits().total().value();
        } else {
            total = content.size();
        }

        return new SearchResponse<>(
                content,
                total,
                page,
                size,
                System.currentTimeMillis()
        );
    }
}