package develop.circlek.util;

public class MySnowflakeUtil {
    private static final long EPOCH = 1609459200000L;
    private static long sequence = 0L;
    private static final long MACHINE_ID = 1L;

    public static synchronized Long generateId() {
        long timestamp = System.currentTimeMillis() - EPOCH;
        sequence = (sequence + 1) & 4095;
        return (timestamp << 22) | (MACHINE_ID << 12) | sequence;
    }
}