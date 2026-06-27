/**
 * CacheEntry represents one key-value record inside the cache.
 *
 * The class is intentionally small and encapsulated. Its fields are private,
 * and other classes access the values through getters. The constructor uses
 * the this keyword because the parameter names shadow the field names.
 */
public class CacheEntry {
    private String key;
    private String value;
    private long timestamp;

    /**
     * Constructs a cache entry.
     *
     * The parameter names key, value, and timestamp are the same as the field
     * names. Without this.key, this.value, and this.timestamp, Java would treat
     * key = key as assigning the parameter to itself, leaving the instance field
     * unchanged.
     */
    public CacheEntry(String key, String value, long timestamp) {
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setValue(String value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Demonstrates Garbage Collection concepts for the assignment.
     *
     * Note: finalize() is deprecated in modern Java, so this is included only
     * because the project specifically asks for a GC demonstration. Java does
     * not guarantee exactly when, or even if, this method will run.
     */
    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        System.out.println("Garbage Collector is destroying evicted entry: " + this.key);
        super.finalize();
    }
}
