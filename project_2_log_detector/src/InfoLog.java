/**
 * InfoLog represents normal operational messages.
 */
public class InfoLog extends LogEntry {
    public InfoLog(String timestamp, String level, String source, String message) {
        super(timestamp, level, source, message);
    }

    public String getCategory() {
        return "INFO";
    }

    public boolean isCriticalSignal() {
        return false;
    }

    public String getAnomalyType() {
        return "NONE";
    }

    public String getAnomalyKey() {
        return "NORMAL";
    }

    public String getShortDescription() {
        return getTimestamp() + " INFO " + getSource() + " - " + getMessage();
    }
}
