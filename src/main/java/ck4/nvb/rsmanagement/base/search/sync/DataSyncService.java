package ck4.nvb.rsmanagement.base.search.sync;

import ck4.nvb.rsmanagement.base.search.annotation.SearchableEntity;
import ck4.nvb.rsmanagement.base.search.base.BaseSearchDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("dataSyncService")
public class DataSyncService {
  @Autowired private ElasticsearchClient elasticsearchClient;

  /** Sync single entity to Elasticsearch (async) - Generic version */
  @Async
  public <E, T extends BaseSearchDocument<ID>, ID> void syncEntity(
      E entity, Class<T> documentClass) {
    try {
      T document = convertEntityToDocument(entity, documentClass);
      String indexName = resolveIndexName(documentClass);

      IndexResponse resp =
          elasticsearchClient.index(
              i ->
                  i.index(indexName)
                      .id(document.getId() != null ? document.getId().toString() : null)
                      .document(document));

    } catch (Exception e) {
      System.err.println("Error syncing entity to Elasticsearch: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Sync single entity to Elasticsearch (async) - Raw type version for event listeners */
  @Async
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void syncEntityFromEvent(Object entity, Class<?> documentClass) {
    try {
      // Runtime type checking
      if (!BaseSearchDocument.class.isAssignableFrom(documentClass)) {
        throw new IllegalArgumentException("Document class must extend BaseSearchDocument");
      }

      // Use raw types to avoid generic type bounds issues
      syncEntityRaw(entity, documentClass);

    } catch (Exception e) {
      System.err.println("Error syncing entity from event: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Internal raw type version to handle event-driven sync */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void syncEntityRaw(Object entity, Class documentClass) {
    try {
      BaseSearchDocument document = convertEntityToDocumentRaw(entity, documentClass);
      String indexName = resolveIndexName(documentClass);

      IndexResponse resp =
          elasticsearchClient.index(
              i ->
                  i.index(indexName)
                      .id(document.getId() != null ? document.getId().toString() : null)
                      .document(document));

    } catch (Exception e) {
      System.err.println("Error syncing entity to Elasticsearch (raw): " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Bulk sync entities (async). */
  @Async
  public <E, T extends BaseSearchDocument<ID>, ID> void bulkSyncEntities(
      List<E> entities, Class<T> documentClass) {
    if (entities == null || entities.isEmpty()) return;

    try {
      List<T> documents =
          entities.stream()
              .map(e -> convertEntityToDocument(e, documentClass))
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      if (documents.isEmpty()) return;

      String indexName = resolveIndexName(documentClass);

      List<BulkOperation> ops = new ArrayList<>(documents.size());
      for (T doc : documents) {
        ops.add(
            BulkOperation.of(
                b ->
                    b.index(
                        idx ->
                            idx.index(indexName)
                                .id(doc.getId() != null ? doc.getId().toString() : null)
                                .document(doc))));
      }

      BulkResponse bulkResp = elasticsearchClient.bulk(b -> b.operations(ops));

      if (bulkResp.errors()) {
        StringBuilder sb = new StringBuilder("Bulk index errors:\n");
        bulkResp
            .items()
            .forEach(
                item -> {
                  if (item.error() != null) {
                    sb.append("id=")
                        .append(item.id())
                        .append(", index=")
                        .append(item.index())
                        .append(", status=")
                        .append(item.status())
                        .append(", result=")
                        .append(item.result())
                        .append(item.error() != null ? (", error=" + item.error().reason()) : "")
                        .append("\n");
                  }
                });
        System.err.println(sb.toString());
      } else {
        System.out.println(
            "Bulk synced "
                + documents.size()
                + " entities to Elasticsearch (index="
                + indexName
                + ")");
      }
    } catch (Exception e) {
      System.err.println("Error bulk syncing entities to Elasticsearch: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Delete from Elasticsearch (async) */
  @Async
  public void deleteFromElasticsearch(String id, String indexName) {
    if (id == null || indexName == null) return;
    try {
      elasticsearchClient.delete(d -> d.index(indexName).id(id));
    } catch (Exception e) {
      System.err.println("Error deleting from Elasticsearch: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Check if index exists */
  public boolean indexExists(Class<?> documentClass) {
    String indexName = resolveIndexName(documentClass);
    try {
      var existsResp = elasticsearchClient.indices().exists(b -> b.index(indexName));
      return existsResp != null && Boolean.TRUE.equals(existsResp.value());
    } catch (Exception e) {
      System.err.println("Error while checking index exists: " + e.getMessage());
      return false;
    }
  }

  /** Create index if not exists */
  public void createIndexIfNotExists(Class<?> documentClass) {
    String indexName = resolveIndexName(documentClass);
    try {
      if (!indexExists(documentClass)) {
        elasticsearchClient.indices().create(c -> c.index(indexName));
        System.out.println("Created index: " + indexName);
      }
    } catch (Exception e) {
      System.err.println("Error while creating index '" + indexName + "': " + e.getMessage());
      e.printStackTrace();
    }
  }

  // ----------------------
  // Helper & conversion
  // ----------------------

  protected String resolveIndexName(Class<?> documentClass) {
    SearchableEntity meta = documentClass.getAnnotation(SearchableEntity.class);
    if (meta != null && meta.indexName() != null && !meta.indexName().isEmpty()) {
      return meta.indexName();
    }
    return documentClass.getSimpleName().toLowerCase();
  }

  @SuppressWarnings("unchecked")
  private <E, T extends BaseSearchDocument<ID>, ID> T convertEntityToDocument(
      E entity, Class<T> documentClass) {
    try {
      Constructor<T> constructor = documentClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      T document = constructor.newInstance();
      document.fromEntity(entity);
      return document;
    } catch (Exception e) {
      throw new RuntimeException("Error converting entity to document", e);
    }
  }

  /** Raw type version for event-driven conversion */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private BaseSearchDocument convertEntityToDocumentRaw(Object entity, Class documentClass) {
    try {
      Constructor constructor = documentClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      BaseSearchDocument document = (BaseSearchDocument) constructor.newInstance();
      document.fromEntity(entity);
      return document;
    } catch (Exception e) {
      throw new RuntimeException("Error converting entity to document (raw)", e);
    }
  }
}
