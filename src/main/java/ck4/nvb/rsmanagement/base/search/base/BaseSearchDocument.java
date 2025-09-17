package ck4.nvb.rsmanagement.base.search.base;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSearchDocument<ID> {
  @Id protected ID id;

  /**
   * Convert from JPA entity -> search document
   *
   * @param entity
   */
  public abstract void fromEntity(Object entity);

  /**
   * Get search content to build query
   *
   * @return
   */
  public abstract String getSearchableContent();
}
