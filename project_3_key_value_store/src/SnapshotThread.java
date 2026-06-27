import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Background daemon that periodically saves the cache state to snapshot.txt.
 */
public class SnapshotThread extends Thread {
    private CacheStore store;
    private String fileName;
    private volatile boolean running;

    public SnapshotThread(CacheStore store, String fileName) {
        this.store = store;
        this.fileName = fileName;
        this.running = true;
        setDaemon(true);
        setName("SnapshotThread");
    }

    public void shutdown() {
        this.running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(2000);
                saveSnapshot();
            } catch (InterruptedException e) {
                if (!running) {
                    return;
                }
            }
        }
    }

    public void saveSnapshot() {
        List<CacheEntry> copy = store.snapshotEntries();
        StringBuilder builder = new StringBuilder();
        builder.append("Snapshot saved by ").append(getName()).append("\n");
        builder.append("Entries: ").append(copy.size()).append("\n");
        builder.append("Total operations: ").append(CacheStore.getTotalOperations()).append("\n");
        builder.append("----------------------------------------\n");

        for (CacheEntry entry : copy) {
            builder.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append(" (timestamp: ")
                    .append(entry.getTimestamp())
                    .append(")\n");
        }

        try (FileOutputStream output = new FileOutputStream(fileName)) {
            output.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("SnapshotThread saved " + copy.size() + " entries to " + fileName);
        } catch (IOException e) {
            System.out.println("SnapshotThread failed to save file: " + e.getMessage());
        }
    }
}
