package ck4.nvb.rsmanagement.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
@Profile("data-generator")
public class AdjustableClock extends Clock {
    private Instant currentInstant;
    private final ZoneId zoneId;

    public AdjustableClock() {
        this.zoneId = ZoneId.systemDefault();
        this.currentInstant = LocalDate.now(zoneId).atStartOfDay(zoneId).toInstant();
    }

    public AdjustableClock(Instant fixedInstant, ZoneId zone) {
        this.currentInstant = fixedInstant;
        this.zoneId = zone;
    }

    // method cộng thêm ngày/giờ vào đồng hồ
    public void plusDays(long days) {
        this.currentInstant = this.currentInstant.plus(Duration.ofDays(days));
    }

    public void plusHours(long hours) {
        this.currentInstant = this.currentInstant.plus(Duration.ofHours(hours));
    }

    public void reset(LocalDateTime newDateTime) {
        this.currentInstant = newDateTime.toInstant(zoneId.getRules().getOffset(newDateTime));
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new AdjustableClock(currentInstant, zone);
    }

    @Override
    public Instant instant() {
        return currentInstant;
    }
}
