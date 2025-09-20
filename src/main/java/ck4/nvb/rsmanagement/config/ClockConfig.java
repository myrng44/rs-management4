package ck4.nvb.rsmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
@Configuration
@Profile("dev")
public class ClockConfig {
    @Bean
    @Primary
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
