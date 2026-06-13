import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchEngine.java
 *
 * Usage:
 *   java SearchEngine <directoryPath> <searchTerm>
 *
 * Main entry point for the multi-threaded local search and indexing engine.
 */
public class SearchEngine {

    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SearchEngine <directoryPath> <searchTerm>");
            return;
        }

        String directoryPath = args[0];
        String searchTerm = args[1];

        File rootDir = new File(directoryPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.out.println("Error: '" + directoryPath + "' is not a valid directory.");
            return;
        }

        List<SearchResult> sharedResults = new ArrayList<>();
        List<File> allFiles = new ArrayList<>();
        collectFiles(rootDir, allFiles);

        System.out.println("Found " + allFiles.size() + " files under '" + directoryPath + "'.");
        System.out.println("Searching for: \"" + searchTerm + "\" using " + THREAD_COUNT + " worker thread(s)...\n");

        if (allFiles.isEmpty()) {
            System.out.println("No files found to search.");
            return;
        }

        int threadsToUse = Math.max(1, Math.min(THREAD_COUNT, allFiles.size()));
        List<List<File>> chunks = splitIntoChunks(allFiles, threadsToUse);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsToUse; i++) {
            String workerName = "Worker-" + (i + 1);
            SearchWorker worker = new SearchWorker(chunks.get(i), searchTerm, sharedResults, workerName);
            Thread t = new Thread(worker, workerName);
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting for " + t.getName());
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n--- Search Complete ---");
        System.out.println("Total matches found: " + sharedResults.size());
        System.out.println("------------------------\n");

        for (SearchResult result : sharedResults) {
            System.out.println(result);
        }

        writeResultsToCsv(sharedResults, "results.csv");
    }

    private static void collectFiles(File dir, List<File> collected) {
        File[] entries = dir.listFiles();
        if (entries == null) {
            System.out.println("Warning: cannot list directory: " + dir.getPath());
            return;
        }

        for (File entry : entries) {
            if (entry.isDirectory()) {
                collectFiles(entry, collected);
            } else if (entry.getName().endsWith(".txt") || entry.getName().endsWith(".java")) {
                collected.add(entry);
            }
        }
    }

    private static List<List<File>> splitIntoChunks(List<File> files, int numChunks) {
        List<List<File>> chunks = new ArrayList<>();
        for (int i = 0; i < numChunks; i++) {
            chunks.add(new ArrayList<>());
        }
        for (int i = 0; i < files.size(); i++) {
            chunks.get(i % numChunks).add(files.get(i));
        }
        return chunks;
    }

    private static void writeResultsToCsv(List<SearchResult> results, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("FileName,LineNumber,Snippet\n");
            for (SearchResult r : results) {
                String safeSnippet = r.getLineSnippet() == null
                    ? ""
                    : r.getLineSnippet().trim().replace("\"", "\"\"");
                writer.write(r.getFileName() + "," + r.getLineNumber() + ",\"" + safeSnippet + "\"\n");
            }
            System.out.println("\nResults written to " + outputPath);
        } catch (IOException e) {
            System.out.println("Warning: could not write CSV output: " + e.getMessage());
        }
    }
}
