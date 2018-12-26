import java.sql.Timestamp;

public class UserSnapshot {

    private String userName;
    private Timestamp timestamp;

    public UserSnapshot(String userName, Timestamp timestamp) {
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserSnapshot{" +
                "userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                "}\n";
    }
}
