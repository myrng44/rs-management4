package ck4.nvb.rsmanagement.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class FlywayConfig {

  @Value("${spring.flyway.locations}")
  private String[] location;

  @Value("${spring.datasource.url}")
  private String dataSourceUrl;

  @Value("${spring.datasource.username}")
  private String dataSourceUsername;

  @Value("${spring.datasource.password}")
  private String dataSourcePassword;

  @Bean
  public Flyway flyway() {
    Flyway flyway =
        Flyway.configure()
            .dataSource(dataSource())
            .locations(location)
            .baselineOnMigrate(true)
            .load();
    flyway.migrate();
    return flyway; // rum sql file khi version is new (vd: V3 >V2 >V1...)
  }

  @Bean
  public DataSource dataSource() {
    System.out.println(">>> Datasource URL in use: " + dataSourceUrl);
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(dataSourceUrl);
    dataSource.setUsername(dataSourceUsername);
    dataSource.setPassword(dataSourcePassword);
    return dataSource;
  }
}
