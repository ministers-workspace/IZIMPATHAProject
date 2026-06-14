public class SearchResult {

    private final String fileName;
    private final int lineNumber;
    private final String lineSnippet;

    public SearchResult(String fileName, int lineNumber, String lineSnippet) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.lineSnippet = lineSnippet;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineSnippet() {
        return lineSnippet;
    }

    @Override
    public String toString() {
        return fileName + ":" + lineNumber + ": " + lineSnippet;
    }
}
