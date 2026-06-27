/**
 * Main starts the thread-safe key-value store demonstration.
 *
 * Usage:
 *   java Main 123456
 *
 * The program calculates C as the sum of the digits in the supplied student ID.
 * If no student ID is supplied, C defaults to the assignment minimum of 5.
 */
public class Main {
    private static final int WRITES_PER_USER = 100;

    public static void main(String[] args) {
        String studentId = args.length > 0 ? args[0] : "00000";
        int concurrentUsers = calculateConcurrentUsers(studentId);
        int expectedEntries = concurrentUsers * WRITES_PER_USER;

        System.out.println("Student ID used: " + studentId);
        System.out.println("C concurrent user threads: " + concurrentUsers);
        System.out.println("Expected entries after writes: " + expectedEntries);

        CacheStore store = new CacheStore();
        SnapshotThread snapshotThread = new SnapshotThread(store, "snapshot.txt");
        snapshotThread.start();

        Thread[] users = new Thread[concurrentUsers];
        for (int i = 0; i < concurrentUsers; i++) {
            users[i] = new Thread(new UserThread(store, i + 1, WRITES_PER_USER), "UserThread-" + (i + 1));
            users[i].start();
        }

        for (Thread user : users) {
            try {
                user.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread was interrupted while waiting for users.");
                return;
            }
        }

        // Force a final snapshot after all writers finish so the execution log
        // proves that snapshot.txt contains the completed cache state.
        snapshotThread.saveSnapshot();
        snapshotThread.shutdown();

        System.out.println("Actual entries after writes: " + store.size());
        System.out.println("Total operations performed globally: " + CacheStore.getTotalOperations());

        if (store.size() == expectedEntries) {
            System.out.println("SUCCESS: synchronized put() protected every write. No data was lost.");
        } else {
            System.out.println("ERROR: cache size mismatch. Synchronization must be reviewed.");
        }

        demonstrateGarbageCollection();
    }

    public static int calculateConcurrentUsers(String studentId) {
        int sum = 0;
        for (int i = 0; i < studentId.length(); i++) {
            char ch = studentId.charAt(i);
            if (Character.isDigit(ch)) {
                sum += Character.getNumericValue(ch);
            }
        }
        return Math.max(sum, 5);
    }

    /**
     * Creates temporary objects and requests garbage collection.
     * This demonstrates eligibility for GC, but Java does not guarantee that
     * finalize() messages will appear every time the program runs.
     */
    public static void demonstrateGarbageCollection() {
        for (int i = 0; i < 1000; i++) {
            new CacheEntry("temporary-" + i, "garbage", System.currentTimeMillis());
        }
        System.gc();
        System.out.println("GC requested after creating temporary CacheEntry objects.");
    }
}
