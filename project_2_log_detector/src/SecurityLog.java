/**
 * SecurityLog represents authentication and access-control events.
 */
public class SecurityLog extends LogEntry {
    private String username;
    private String ipAddress;
    private String action;

    public SecurityLog(String timestamp, String level, String source, String username,
            String ipAddress, String action, String message) {
        super(timestamp, level, source, message);
        this.username = username;
        this.ipAddress = ipAddress;
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getAction() {
        return action;
    }

    public String getCategory() {
        return "SECURITY";
    }

    /**
     * Failed logins are treated as brute-force signals.
     */
    public boolean isCriticalSignal() {
        switch (action.charAt(0)) {
            case 'F':
                if (action.equals("FAILED_LOGIN")) {
                    return true;
                }
                break;
            case 'D':
                if (action.equals("DENIED")) {
                    return true;
                }
                break;
            default:
                return false;
        }

        return false;
    }

    public String getAnomalyType() {
        if (isCriticalSignal()) {
            return "BRUTE_FORCE";
        }

        return "NONE";
    }

    /**
     * Group failed logins by username and IP address.
     */
    public String getAnomalyKey() {
        return username + "@" + ipAddress;
    }

    public String getShortDescription() {
        return getTimestamp() + " SECURITY " + sourceLabel() + " user=" + username
                + " ip=" + ipAddress + " action=" + action + " - " + getMessage();
    }

    private String sourceLabel() {
        return getSource();
    }
}
