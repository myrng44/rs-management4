package ck4.nvb.rsmanagement.auth.service;
import org.springframework.stereotype.Component;
import xyz.downgoon.snowflake.Snowflake;

@Component
public class SnowFlakeIdGenerator {

    private final Snowflake snowflake;

    public SnowFlakeIdGenerator() {
        // Bạn có thể truyền workerId, datacenterId từ properties
        this.snowflake = new Snowflake(1, 1);
    }

    public long generateId() {
        return snowflake.nextId();
    }
}
