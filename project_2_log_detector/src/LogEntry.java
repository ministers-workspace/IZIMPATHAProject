/**
 * LogEntry is the abstract parent for every parsed log object.
 * The anomaly detector stores LogEntry references so it can scan all
 * subclasses through one common type.
 */
public abstract class LogEntry {
    private String timestamp;
    private String level;
    private String source;
    private String message;
    private int secondStamp;

    /**
     * Builds a common log record and converts the timestamp into a comparable
     * second value. The conversion is manual to avoid date libraries.
     */
    public LogEntry(String timestamp, String level, String source, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
        this.secondStamp = calculateSecondStamp(timestamp);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public int getSecondStamp() {
        return secondStamp;
    }

    /**
     * Converts "yyyy-mm-dd hh:mm:ss" into a rough ordered second value.
     * The assignment uses simulated logs, so exact calendar month lengths
     * are unnecessary; the value only needs to preserve ordering.
     */
    private int calculateSecondStamp(String value) {
        int year = numberBetween(value, 0, 4);
        int month = numberBetween(value, 5, 7);
        int day = numberBetween(value, 8, 10);
        int hour = numberBetween(value, 11, 13);
        int minute = numberBetween(value, 14, 16);
        int second = numberBetween(value, 17, 19);

        return (((((year * 12) + month) * 31 + day) * 24 + hour) * 60 + minute) * 60 + second;
    }

    /**
     * Parses digits manually with char extraction instead of using regex.
     */
    private int numberBetween(String value, int start, int end) {
        int number = 0;
        int index = start;

        while (index < end && index < value.length()) {
            char current = value.charAt(index);
            if (current >= '0' && current <= '9') {
                number = (number * 10) + (current - '0');
            }
            index++;
        }

        return number;
    }

    /**
     * Utility used by subclasses when classifying their own messages.
     */
    protected boolean containsKeyword(String keyword) {
        return message.toLowerCase().indexOf(keyword.toLowerCase()) >= 0;
    }

    /**
     * Dynamic dispatch calls the subclass version at runtime.
     */
    public abstract String getCategory();

    /**
     * Dynamic dispatch lets each subclass decide if it matters to detection.
     */
    public abstract boolean isCriticalSignal();

    /**
     * Returns the type of alert that this log can contribute to.
     */
    public abstract String getAnomalyType();

    /**
     * Returns the grouping key for related suspicious logs.
     */
    public abstract String getAnomalyKey();

    /**
     * Gives a compact line for console output.
     */
    public abstract String getShortDescription();

    /**
     * Compares two entries using behavior supplied by the concrete subclass.
     */
    public boolean matchesAnomalyFamily(LogEntry other) {
        return getAnomalyType().equals(other.getAnomalyType())
                && getAnomalyKey().equals(other.getAnomalyKey());
    }
}
