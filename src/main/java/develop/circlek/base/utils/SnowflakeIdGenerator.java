package develop.circlek.base.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;

public class SnowflakeIdGenerator implements IdentifierGenerator {
    private static final long EPOCH = 1609459200000L;
    private static long sequence = 0L;
    private static final long MACHINE_ID = 1L;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return generateId();
    }
    
    public static synchronized Long generateId() {
        long timestamp = System.currentTimeMillis() - EPOCH;
        sequence = (sequence + 1) & 4095;
        return (timestamp << 22) | (MACHINE_ID << 12) | sequence;
    }
}