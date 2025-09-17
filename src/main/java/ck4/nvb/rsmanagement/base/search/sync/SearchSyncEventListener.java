package ck4.nvb.rsmanagement.base.search.sync;

import ck4.nvb.rsmanagement.base.search.annotation.SearchableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SearchSyncEventListener {
  @Autowired private DataSyncService dataSyncService;

  @EventListener
  public void handleEntityCreate(SyncEvent.EntityCreated event) {
    Object entity = event.getEntity();
    Class<?> entityClass = entity.getClass();

    SearchableEntity annotation = entityClass.getAnnotation(SearchableEntity.class);
    if (annotation != null && annotation.autoSync()) {
      dataSyncService.syncEntityFromEvent(entity, event.getDocumentClass());
    }
  }

  @EventListener
  public void handleEntityUpdate(SyncEvent.EntityUpdated event) {
    Object entity = event.getEntity();
    Class<?> entityClass = entity.getClass();

    SearchableEntity annotation = entityClass.getAnnotation(SearchableEntity.class);
    if (annotation != null && annotation.autoSync()) {
      dataSyncService.syncEntityFromEvent(entity, event.getDocumentClass());
    }
  }

  @EventListener
  public void handleEntityDelete(SyncEvent.EntityDeleted event) {
    Class<?> entityClass = event.getEntityClass();

    SearchableEntity annotation = entityClass.getAnnotation(SearchableEntity.class);
    if (annotation != null && annotation.autoSync()) {
      dataSyncService.deleteFromElasticsearch(
          event.getEntityId().toString(), annotation.indexName());
    }
  }
}
