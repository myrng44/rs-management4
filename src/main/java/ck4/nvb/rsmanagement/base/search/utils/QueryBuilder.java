package ck4.nvb.rsmanagement.base.search.utils;

import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import java.util.Arrays;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("queryBuilder")
public class QueryBuilder {

  /**
   * build simple text query
   *
   * @param keyword
   * @param fields
   * @return
   */
  public Query buildSimpleTextQuery(String keyword, String[] fields) {
    return MultiMatchQuery.of(
            m ->
                m.query(keyword)
                    .fields(Arrays.asList(fields))
                    .type(TextQueryType.BestFields)
                    .fuzziness("AUTO"))
        ._toQuery();
  }

  /**
   * build fuzzy query
   *
   * @param keyword
   * @param fields
   * @return
   */
  public Query buildFuzzyQuery(String keyword, String[] fields) {
    return BoolQuery.of(
            b -> {
              Arrays.stream(fields)
                  .forEach(
                      field ->
                          b.should(
                              Query.of(
                                  q ->
                                      q.fuzzy(
                                          f ->
                                              f.field(field)
                                                  .value(keyword)
                                                  .fuzziness("AUTO")
                                                  .prefixLength(1)
                                                  .maxExpansions(50)))));
              return b;
            })
        ._toQuery();
  }

  /**
   * build advanced query với filter
   *
   * @param request
   * @param searchFields
   * @return
   */
  public Query buildAdvancedQuery(SearchRequest request, String[] searchFields) {
    return BoolQuery.of(
            b -> {
              // Text search
              if (request.hasTextQuery()) {
                Query textQuery =
                    request.isFuzzy()
                        ? buildFuzzyQuery(request.getKeyword(), searchFields)
                        : buildSimpleTextQuery(request.getKeyword(), searchFields);
                b.must(textQuery);
              }

              // Apply filters
              for (Map.Entry<String, Object> filter : request.getFilters().entrySet()) {
                Query filterQuery = buildFilterQuery(filter.getKey(), filter.getValue());
                b.filter(filterQuery);
              }
              return b;
            })
        ._toQuery();
  }

  /**
   * build prefix query cho autocomplete
   *
   * @param prefix
   * @param field
   * @return
   */
  public Query buildPrefixQuery(String prefix, String field) {
    return PrefixQuery.of(p -> p.field(field).value(prefix.toLowerCase()))._toQuery();
  }

  /**
   * build range query
   *
   * @param field
   * @param from
   * @param to
   * @return
   */
  public Query buildRangeQuery(String field, Object from, Object to) {
    return Query.of(
        q ->
            q.range(
                r ->
                    r.untyped(
                        u -> {
                          u.field(field);
                          if (from != null) {
                            u.gte(JsonData.of(from));
                          }
                          if (to != null) {
                            u.lte(JsonData.of(to));
                          }
                          return u;
                        })));
  }

  /**
   * build filter query base on value type
   *
   * @param field
   * @param value
   * @return
   */
  private Query buildFilterQuery(String field, Object value) {
    if (value instanceof String || value instanceof Number || value instanceof Boolean) {
      return TermQuery.of(t -> t.field(field).value(value.toString()))._toQuery();
    } else if (value instanceof Map) {
      // Range query
      @SuppressWarnings("unchecked")
      Map<String, Object> range = (Map<String, Object>) value;
      return buildRangeQuery(field, range.get("from"), range.get("to"));
    }

    return TermQuery.of(t -> t.field(field).value(value.toString()))._toQuery();
  }
}
