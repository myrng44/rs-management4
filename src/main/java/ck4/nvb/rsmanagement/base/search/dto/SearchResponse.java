package ck4.nvb.rsmanagement.base.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchResponse<T> {
  private List<T> content;
  private long totalElements;
  private int page;
  private int size;
  private int totalPages;
  private long searchTime;
  private String searchMethod;

  public SearchResponse(List<T> content, long totalElements, int page, int size, long searchTime) {
    this.content = content;
    this.totalElements = totalElements;
    this.page = page;
    this.size = size;
    this.totalPages = (int) Math.ceil((double) totalElements / (double) size);
    this.searchTime = searchTime;
  }

  public boolean isEmpty() {
    return content == null || content.isEmpty();
  }

  public static <T> SearchResponse<T> empty() {
    return new SearchResponse<>(List.of(), 0, 0, 0, 0);
  }
}
