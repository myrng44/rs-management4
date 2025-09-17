package ck4.nvb.rsmanagement.base.search.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rs.elasticsearch")
public class ElasticsearchProperties {
  private boolean enabled = true;
  private int defaultPageSize = 20;
  private int maxPageSize = 100;
  private boolean enableFuzzySearch = true;
  private boolean enableAutoSync = true;
  private long syncDelayMs = 1000;

  private Elasticsearch elasticsearch = new Elasticsearch();

  @Setter
  @Getter
  public static class Elasticsearch {
    // Getters and setters
    private boolean enabled = true;
    private int bulkSize = 100;
    private String refreshInterval = "1s";
    private int maxRetries = 3;
    private long retryDelayMs = 5000;
    private boolean fallbackToDatabase = true;
  }
}
