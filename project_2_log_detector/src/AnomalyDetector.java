import java.util.ArrayList;
import java.util.Iterator;

/**
 * AnomalyDetector scans parsed LogEntry objects with Iterators.
 */
public class AnomalyDetector {
    private int threshold;

    public AnomalyDetector(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Uses nested Iterator loops to find A critical signals in a 60-second window.
     */
    public ArrayList<String> detect(ArrayList<LogEntry> logs) {
        ArrayList<String> alerts = new ArrayList<String>();
        ArrayList<String> reportedAlertIds = new ArrayList<String>();
        Iterator<LogEntry> startIterator = logs.iterator();

        while (startIterator.hasNext()) {
            LogEntry windowStart = startIterator.next();

            if (windowStart.isCriticalSignal()) {
                int count = 0;
                ArrayList<String> samples = new ArrayList<String>();
                Iterator<LogEntry> scanIterator = logs.iterator();

                while (scanIterator.hasNext()) {
                    LogEntry candidate = scanIterator.next();

                    if (candidate.isCriticalSignal()) {
                        if (windowStart.matchesAnomalyFamily(candidate)) {
                            int secondsApart = candidate.getSecondStamp() - windowStart.getSecondStamp();

                            if (secondsApart >= 0) {
                                if (secondsApart <= 60) {
                                    count++;
                                    samples.add(candidate.getShortDescription());
                                }
                            }
                        }
                    }
                }

                if (count >= threshold) {
                    String alertId = windowStart.getAnomalyType() + ":" + windowStart.getAnomalyKey()
                            + ":" + windowStart.getSecondStamp();

                    if (!reportedAlertIds.contains(alertId)) {
                        reportedAlertIds.add(alertId);
                        alerts.add(buildAlert(windowStart, count, samples));
                    }
                }
            }
        }

        return alerts;
    }

    /**
     * Creates a readable alert while still relying on polymorphic LogEntry methods.
     */
    private String buildAlert(LogEntry windowStart, int count, ArrayList<String> samples) {
        String headline;

        switch (windowStart.getAnomalyType()) {
            case "BRUTE_FORCE":
                headline = "BRUTE-FORCE ATTACK DETECTED";
                break;
            case "SERVER_CRASH":
                headline = "SERVER CRASH / MELTDOWN DETECTED";
                break;
            default:
                headline = "UNKNOWN ANOMALY DETECTED";
                break;
        }

        String alert = headline + "\n"
                + "  Key: " + windowStart.getAnomalyKey() + "\n"
                + "  Window started: " + windowStart.getTimestamp() + "\n"
                + "  Matching events inside 60 seconds: " + count + "\n"
                + "  First sample: " + samples.get(0);

        return alert;
    }
}
