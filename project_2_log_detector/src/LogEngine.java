import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main driver for the automated log parsing engine.
 */
public class LogEngine {
    public static void main(String[] args) {
        int firstDigit = readFirstStudentDigit(args);
        int threshold = firstDigit + 4;

        ArrayList<String> rawLogs = buildSimulatedLogs(threshold);
        ArrayList<LogEntry> parsedLogs = new ArrayList<LogEntry>();
        LogParser parser = new LogParser();

        Iterator<String> rawIterator = rawLogs.iterator();
        while (rawIterator.hasNext()) {
            String rawLine = rawIterator.next();
            parsedLogs.add(parser.parse(rawLine));
        }

        printParsedSummary(parsedLogs, firstDigit, threshold);

        AnomalyDetector detector = new AnomalyDetector(threshold);
        ArrayList<String> alerts = detector.detect(parsedLogs);

        System.out.println();
        System.out.println("ANOMALY RESULTS");
        System.out.println("===============");

        Iterator<String> alertIterator = alerts.iterator();
        while (alertIterator.hasNext()) {
            System.out.println(alertIterator.next());
            System.out.println();
        }

        if (alerts.size() == 0) {
            System.out.println("No cyberattack or server meltdown detected.");
        }
    }

    /**
     * Reads the first digit from the command line. Example: java LogEngine 6
     */
    private static int readFirstStudentDigit(String[] args) {
        int defaultDigit = 3;

        if (args.length == 0) {
            return defaultDigit;
        }

        if (args[0].length() == 0) {
            return defaultDigit;
        }

        char digit = args[0].charAt(0);
        if (digit >= '0' && digit <= '9') {
            return digit - '0';
        }

        return defaultDigit;
    }

    /**
     * Builds test data with enough records to trigger both required alerts.
     */
    private static ArrayList<String> buildSimulatedLogs(int threshold) {
        ArrayList<String> logs = new ArrayList<String>();

        logs.add("2026-06-26 09:00:00 | INF | web | message=Health check passed");
        logs.add("2026-06-26 09:00:05 | SEC | auth | user=ayanda ip=10.0.0.12 action=LOGIN_OK message=Normal login");

        int bruteForceIndex = 0;
        while (bruteForceIndex < threshold) {
            String second = twoDigits(10 + bruteForceIndex);
            logs.add("2026-06-26 09:01:" + second
                    + " | SEC | auth | user=admin ip=192.168.1.44 action=FAILED_LOGIN message=Bad password attempt");
            bruteForceIndex++;
        }

        logs.add("2026-06-26 09:02:30 | SEC | auth | user=guest ip=10.0.0.9 action=FAILED_LOGIN message=Single failed login");
        logs.add("2026-06-26 09:03:00 | ERR | web | code=404 message=Missing image file");

        int crashIndex = 0;
        while (crashIndex < threshold) {
            String second = twoDigits(5 + crashIndex);
            logs.add("2026-06-26 09:04:" + second
                    + " | ERR | api | code=500 message=Internal Server Error during checkout");
            crashIndex++;
        }

        logs.add("2026-06-26 09:05:20 | INF | scheduler | message=Nightly cleanup completed");

        return logs;
    }

    /**
     * Pads seconds to two characters using simple String logic.
     */
    private static String twoDigits(int value) {
        if (value < 10) {
            return "0" + value;
        }

        return "" + value;
    }

    /**
     * Shows that all parsed objects live in one ArrayList<LogEntry>.
     */
    private static void printParsedSummary(ArrayList<LogEntry> parsedLogs, int firstDigit, int threshold) {
        System.out.println("AUTOMATED LOG PARSING ENGINE");
        System.out.println("============================");
        System.out.println("Student ID first digit used: " + firstDigit);
        System.out.println("Personalized threshold A = first digit + 4 = " + threshold);
        System.out.println("Total parsed logs: " + parsedLogs.size());
        System.out.println();
        System.out.println("PARSED LOG OBJECTS");
        System.out.println("==================");

        Iterator<LogEntry> iterator = parsedLogs.iterator();
        while (iterator.hasNext()) {
            LogEntry entry = iterator.next();
            System.out.println(entry.getCategory() + " -> " + entry.getShortDescription());
        }
    }
}
