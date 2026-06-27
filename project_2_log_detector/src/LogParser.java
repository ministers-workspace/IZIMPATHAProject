/**
 * LogParser manually converts raw strings into objects.
 * It deliberately uses String methods, charAt, if statements, and switch.
 */
public class LogParser {
    /**
     * Parses lines formatted like:
     * 2026-06-26 09:00:00 | SEC | auth | user=neo ip=10.0.0.4 action=FAILED_LOGIN message=Bad password
     */
    public LogEntry parse(String rawLine) {
        int firstDivider = rawLine.indexOf(" | ");
        int secondDivider = rawLine.indexOf(" | ", firstDivider + 3);
        int thirdDivider = rawLine.indexOf(" | ", secondDivider + 3);

        String timestamp = rawLine.substring(0, firstDivider);
        String type = rawLine.substring(firstDivider + 3, secondDivider);
        String source = rawLine.substring(secondDivider + 3, thirdDivider);
        String details = rawLine.substring(thirdDivider + 3);

        char route = type.charAt(0);

        switch (route) {
            case 'I':
                return new InfoLog(timestamp, type, source, readMessage(details));
            case 'E':
                return new ErrorLog(timestamp, type, source, readIntegerValue(details, "code="),
                        readMessage(details));
            case 'S':
                return new SecurityLog(timestamp, type, source,
                        readTextValue(details, "user="),
                        readTextValue(details, "ip="),
                        readTextValue(details, "action="),
                        readMessage(details));
            default:
                return new InfoLog(timestamp, "INF", source, "Unclassified raw log: " + details);
        }
    }

    /**
     * Reads a key=value token until the next space.
     */
    private String readTextValue(String details, String key) {
        int start = details.indexOf(key);

        if (start < 0) {
            return "UNKNOWN";
        }

        start = start + key.length();
        int end = details.indexOf(" ", start);

        if (end < 0) {
            end = details.length();
        }

        return details.substring(start, end);
    }

    /**
     * Reads a numeric key=value token using character extraction.
     */
    private int readIntegerValue(String details, String key) {
        int start = details.indexOf(key);

        if (start < 0) {
            return 0;
        }

        start = start + key.length();
        int number = 0;
        int index = start;

        while (index < details.length()) {
            char current = details.charAt(index);

            if (current >= '0' && current <= '9') {
                number = (number * 10) + (current - '0');
            } else {
                break;
            }

            index++;
        }

        return number;
    }

    /**
     * Reads everything after message= as the human-readable message.
     */
    private String readMessage(String details) {
        String key = "message=";
        int start = details.indexOf(key);

        if (start < 0) {
            return details;
        }

        return details.substring(start + key.length());
    }
}
