package ck4.nvb.rsmanagement.base.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchableEntity {
  /** Elasticsearch index name */
  String indexName();

  /** Fields to include in search (empty = all fields) */
  String[] searchFields() default {};

  /** ID field name in JPA entity */
  String idField() default "id";

  /** Enable auto-sync to Elasticsearch */
  boolean autoSync() default true;

  /** Batch size for bulk operations */
  int batchSize() default 100;
}
