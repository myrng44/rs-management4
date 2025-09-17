package ck4.nvb.rsmanagement.base.search.sync;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public abstract class SyncEvent extends ApplicationEvent {

  public SyncEvent(Object source) {
    super(source);
  }

  @Getter
  public static class EntityCreated extends SyncEvent {
    private final Object entity;
    private final Class<?> documentClass;

    public EntityCreated(Object source, Object entity, Class<?> documentClass) {
      super(source);
      this.entity = entity;
      this.documentClass = documentClass;
    }
  }

  @Getter
  public static class EntityUpdated extends SyncEvent {
    private final Object entity;
    private final Class<?> documentClass;

    public EntityUpdated(Object source, Object entity, Class<?> documentClass) {
      super(source);
      this.entity = entity;
      this.documentClass = documentClass;
    }
  }

  @Getter
  public static class EntityDeleted extends SyncEvent {
    private final Object entityId;
    private final Class<?> entityClass;

    public EntityDeleted(Object source, Object entityId, Class<?> entityClass) {
      super(source);
      this.entityId = entityId;
      this.entityClass = entityClass;
    }
  }
}
