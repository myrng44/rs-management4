package ck4.nvb.rsmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.*;

@Configuration
@Profile("data-generator")
public class DataGenClockConfig {
    @Bean
    @Primary
    public AdjustableClock adjustableClock() {
        // Bắt đầu từ 01/09/2025 00:00
        return new AdjustableClock(
                LocalDateTime.of(2025, 8, 26, 6, 0, 0)
                        .toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
    }
}
