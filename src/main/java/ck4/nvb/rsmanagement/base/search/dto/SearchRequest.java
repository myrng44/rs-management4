package ck4.nvb.rsmanagement.base.search.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchRequest {
  private String keyword;
  private Map<String, Object> filters = new HashMap<>();
  private String sortField;
  private String sortDirection = "asc";
  private int page = 0;
  private int size = 20;
  private boolean fuzzy = false;

  public SearchRequest(String keyword) {
    this.keyword = keyword;
  }

  public boolean hasTextQuery() {
    return keyword != null && !keyword.trim().isEmpty();
  }

  public SearchRequest addFilter(String field, Object value) {
    filters.put(field, value);
    return this;
  }

  public SearchRequest withPagination(int page, int size) {
    this.page = page;
    this.size = size;
    return this;
  }

  public SearchRequest withSortField(String sortField, String sortDirection) {
    this.sortField = sortField;
    this.sortDirection = sortDirection;
    return this;
  }

  public SearchRequest enableFuzzy() {
    this.fuzzy = true;
    return this;
  }
}
