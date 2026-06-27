/**
 * ErrorLog represents HTTP and server failure records.
 */
public class ErrorLog extends LogEntry {
    private int statusCode;

    public ErrorLog(String timestamp, String level, String source, int statusCode, String message) {
        super(timestamp, level, source, message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCategory() {
        return "ERROR";
    }

    /**
     * Server-side HTTP 500 errors and exception keywords are meltdown signals.
     */
    public boolean isCriticalSignal() {
        if (statusCode >= 500) {
            return true;
        } else {
            if (containsKeyword("exception")) {
                return true;
            }
        }

        return false;
    }

    public String getAnomalyType() {
        if (isCriticalSignal()) {
            return "SERVER_CRASH";
        }

        return "NONE";
    }

    /**
     * Group all 500-class errors by source service.
     */
    public String getAnomalyKey() {
        return getSource();
    }

    public String getShortDescription() {
        return getTimestamp() + " ERROR " + getSource() + " code=" + statusCode + " - " + getMessage();
    }
}
