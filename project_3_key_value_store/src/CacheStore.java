import java.util.ArrayList;
import java.util.List;

/**
 * CacheStore is a thread-safe in-memory key-value store.
 *
 * The stored entries are instance data because each CacheStore object should own
 * its own cache contents. The totalOperations field is static because it counts
 * all operations globally across every CacheStore object in the program.
 */
public class CacheStore {
    private ArrayList<CacheEntry> entries;
    private static int totalOperations = 0;

    public CacheStore() {
        this.entries = new ArrayList<CacheEntry>();
    }

    /**
     * Adds a new key or updates the existing key.
     *
     * synchronized is used on the whole put method so only one UserThread can
     * own this CacheStore object's monitor while changing the ArrayList. In this
     * specific put() method, the monitor prevents data loss by stopping two
     * threads from simultaneously searching for the same missing key and both
     * appending duplicate CacheEntry objects, or from one thread resizing the
     * ArrayList while another thread is also writing to it.
     */
    public synchronized void put(String key, String value) {
        for (CacheEntry entry : entries) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                totalOperations++;
                return;
            }
        }

        entries.add(new CacheEntry(key, value, System.currentTimeMillis()));
        totalOperations++;
    }

    /**
     * Reads a value safely while other threads may be writing.
     */
    public synchronized String get(String key) {
        for (CacheEntry entry : entries) {
            if (entry.getKey().equals(key)) {
                totalOperations++;
                return entry.getValue();
            }
        }
        totalOperations++;
        return null;
    }

    /**
     * Removes a key safely. This also helps demonstrate Garbage Collection:
     * once a CacheEntry is removed and no references remain, it becomes eligible
     * for collection.
     */
    public synchronized boolean delete(String key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey().equals(key)) {
                entries.remove(i);
                totalOperations++;
                return true;
            }
        }
        totalOperations++;
        return false;
    }

    public synchronized int size() {
        return entries.size();
    }

    /**
     * Returns a defensive copy of the entries for the SnapshotThread.
     *
     * The copy is made while holding the monitor, but the disk-writing happens
     * later outside this synchronized method. This keeps the cache safe without
     * blocking UserThread objects for the full file I/O operation.
     */
    public synchronized List<CacheEntry> snapshotEntries() {
        return new ArrayList<CacheEntry>(entries);
    }

    public static synchronized int getTotalOperations() {
        return totalOperations;
    }
}
