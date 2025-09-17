package ck4.nvb.rsmanagement.base.search.base;

import ck4.nvb.rsmanagement.base.search.annotation.SearchableEntity;
import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import ck4.nvb.rsmanagement.base.search.dto.SearchResponse;
import ck4.nvb.rsmanagement.base.search.sync.DataSyncService;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public abstract class BaseHybridService<E, T extends BaseSearchDocument<ID>, ID> {

  @Autowired protected DataSyncService dataSyncService;

  protected final Class<E> entityClass;
  protected final Class<T> documentClass;
  protected final SearchableEntity searchableConfig;

  @SuppressWarnings("unchecked")
  public BaseHybridService() {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    this.entityClass = (Class<E>) parameterizedType.getActualTypeArguments()[0];
    this.documentClass = (Class<T>) parameterizedType.getActualTypeArguments()[1];
    this.searchableConfig = entityClass.getAnnotation(SearchableEntity.class);

    if (searchableConfig == null) {
      throw new IllegalStateException("Entity must be annotated with @SearchableEntity");
    }
  }

  // === CRUD Operations ===

  @Transactional
  public E create(E entity) {
    // 1. Save to PostgreSQL
    E saved = getJpaRepository().save(entity);

    // 2. Sync to Elasticsearch if enabled
    if (searchableConfig.autoSync()) {
      dataSyncService.syncEntity(saved, documentClass);
    }

    return saved;
  }

  @Transactional
  public E update(ID id, E entity) {
    E existing =
        getJpaRepository()
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found: " + id));

    // Update entity (delegate to concrete implementation)
    E updated = updateEntity(existing, entity);
    E saved = getJpaRepository().save(updated);

    // Sync to Elasticsearch
    if (searchableConfig.autoSync()) {
      dataSyncService.syncEntity(saved, documentClass);
    }

    return saved;
  }

  @Transactional
  public void delete(ID id) {
    // 1. Delete from PostgreSQL
    getJpaRepository().deleteById(id);

    // 2. Delete from Elasticsearch
    if (searchableConfig.autoSync()) {
      dataSyncService.deleteFromElasticsearch(id.toString(), searchableConfig.indexName());
    }
  }

  /** Bulk create entities with optimized Elasticsearch sync */
  @Transactional
  public List<E> bulkCreate(List<E> entities) {
    // 1. Save all to PostgreSQL
    List<E> savedEntities = getJpaRepository().saveAll(entities);

    // 2. Bulk sync to Elasticsearch if enabled
    if (searchableConfig.autoSync()) {
      dataSyncService.bulkSyncEntities(savedEntities, documentClass);
    }

    return savedEntities;
  }

  // === Search Operations ===

  /** Fast text search using Elasticsearch */
  public List<E> fastTextSearch(String keyword) {
    return fastTextSearch(keyword, 0, 100);
  }

  /** Fast text search with pagination using Elasticsearch */
  public List<E> fastTextSearch(String keyword, int page, int size) {
    try {
      // 1. Search in Elasticsearch
      SearchResponse<T> searchResponse = getSearchService().simpleTextSearch(keyword, page, size);

      if (searchResponse.isEmpty()) {
        return List.of();
      }

      // 2. Get IDs and fetch full entities from PostgreSQL
      List<ID> ids =
          searchResponse.getContent().stream()
              .map(BaseSearchDocument::getId)
              .collect(Collectors.toList());

      return findEntitiesByIds(ids);
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute text search", e);
    }
  }

  /** Fuzzy search using Elasticsearch */
  public List<E> fuzzySearch(String keyword) {
    return fuzzySearch(keyword, 0, 20);
  }

  /** Fuzzy search with pagination using Elasticsearch */
  public List<E> fuzzySearch(String keyword, int page, int size) {
    log.debug(
        "BaseHybridService.fuzzySearch called: keyword='{}', page={}, size={}, service={}",
        keyword,
        page,
        size,
        getSearchService().getClass().getSimpleName());
    try {
      SearchResponse<T> searchResponse = getSearchService().fuzzySearch(keyword, page, size);
      log.debug(
          "Elastic returned {} hits",
          searchResponse == null ? "null" : searchResponse.getTotalElements());
      if (searchResponse.isEmpty()) {
        return List.of();
      }
      List<ID> ids =
          searchResponse.getContent().stream()
              .map(BaseSearchDocument::getId)
              .collect(Collectors.toList());
      log.debug("IDs from ES: {}", ids);
      return findEntitiesByIds(ids);
    } catch (IOException e) {
      log.error("Failed to execute fuzzy search via ES", e);
      throw new RuntimeException("Failed to execute fuzzy search", e);
    }
  }

  /** Advanced search with fallback to QueryDSL */
  public List<E> advancedSearch(SearchRequest request) {
    try {
      if (request.hasTextQuery()) {
        // Use Elasticsearch for text search
        SearchResponse<T> searchResponse = getSearchService().advancedSearch(request);

        if (!searchResponse.isEmpty()) {
          List<ID> ids =
              searchResponse.getContent().stream()
                  .map(BaseSearchDocument::getId)
                  .collect(Collectors.toList());
          return findEntitiesByIds(ids);
        }
      }

      // Fallback to structured search with QueryDSL
      return structuredSearch(request);
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute advanced search", e);
    }
  }

  /** Get autocomplete suggestions */
  public List<String> getAutocompleteSuggestions(String prefix, String field) {
    try {
      return getSearchService().getSuggestions(prefix, field);
    } catch (IOException e) {
      throw new RuntimeException("Failed to get autocomplete suggestions", e);
    }
  }

  /** Get paginated search response with both search results and metadata */
  public SearchResponse<E> search(SearchRequest request) {
    try {
      SearchResponse<T> searchResponse = getSearchService().advancedSearch(request);

      if (searchResponse.isEmpty()) {
        return SearchResponse.empty();
      }

      List<ID> ids =
          searchResponse.getContent().stream()
              .map(BaseSearchDocument::getId)
              .collect(Collectors.toList());

      List<E> entities = findEntitiesByIds(ids);

      return new SearchResponse<>(
          entities,
          searchResponse.getTotalElements(),
          searchResponse.getPage(),
          searchResponse.getSize(),
          searchResponse.getSearchTime());
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute search", e);
    }
  }

  /** Manual sync single entity to Elasticsearch */
  public void syncToElasticsearch(E entity) {
    dataSyncService.syncEntity(entity, documentClass);
  }

  /** Manual bulk sync entities to Elasticsearch */
  public void bulkSyncToElasticsearch(List<E> entities) {
    dataSyncService.bulkSyncEntities(entities, documentClass);
  }

  /** Check if Elasticsearch index exists */
  public boolean isIndexExists() {
    return dataSyncService.indexExists(documentClass);
  }

  /** Create Elasticsearch index if not exists */
  public void createIndexIfNotExists() {
    dataSyncService.createIndexIfNotExists(documentClass);
  }

  // === Abstract methods - must implement ===

  /** Get JPA Repository */
  protected abstract JpaRepository<E, ID> getJpaRepository();

  /** Get Search Service */
  protected abstract BaseSearchService<T, ID> getSearchService();

  /** Update entity fields */
  protected abstract E updateEntity(E existing, E newData);

  /** Find entities by list of IDs (should maintain order if needed) */
  protected abstract List<E> findEntitiesByIds(List<ID> ids);

  /** Structured search using QueryDSL (fallback) */
  protected abstract List<E> structuredSearch(SearchRequest request);

  /** Convert entity to search document */
  protected abstract T convertToSearchDocument(E entity);
}
