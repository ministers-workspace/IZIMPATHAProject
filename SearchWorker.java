import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchWorker.java
 *
 * Represents a single worker thread that scans assigned files.
 */
public class SearchWorker implements Runnable {

    private final List<File> filesToScan;
    private final String searchTerm;
    private final List<SearchResult> sharedResults;
    private final String workerName;

    public SearchWorker(List<File> filesToScan, String searchTerm, List<SearchResult> sharedResults, String workerName) {
        this.filesToScan = filesToScan;
        this.searchTerm = searchTerm;
        this.sharedResults = sharedResults;
        this.workerName = workerName;
    }

    @Override
    public void run() {
        for (File file : filesToScan) {
            List<SearchResult> localResults = scanFile(file);
            if (!localResults.isEmpty()) {
                addResultsToSharedList(localResults);
            }
        }
    }

    private List<SearchResult> scanFile(File file) {
        List<SearchResult> localResults = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.contains(searchTerm)) {
                    localResults.add(new SearchResult(file.getName(), lineNumber, line));
                }
            }
        } catch (IOException e) {
            System.out.println("[" + workerName + "] Skipped unreadable file: " + file.getPath() + " (" + e.getMessage() + ")");
        }
        return localResults;
    }

    private void addResultsToSharedList(List<SearchResult> localResults) {
        synchronized (sharedResults) {
            sharedResults.addAll(localResults);
        }
    }
}
