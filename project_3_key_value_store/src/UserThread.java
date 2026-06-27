import java.util.Random;

/**
 * Simulates one user/client writing data to the shared CacheStore.
 */
public class UserThread implements Runnable {
    private CacheStore store;
    private int userNumber;
    private int writeCount;
    private Random random;

    public UserThread(CacheStore store, int userNumber, int writeCount) {
        this.store = store;
        this.userNumber = userNumber;
        this.writeCount = writeCount;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 1; i <= writeCount; i++) {
            String key = "user-" + userNumber + "-key-" + i;
            String value = "value-" + random.nextInt(10000);
            store.put(key, value);

            if (i % 25 == 0) {
                System.out.println(Thread.currentThread().getName() + " wrote " + i + " entries");
            }

            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
