package ck4.nvb.rsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RsManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RsManagementApplication.class, args);
    }
}
