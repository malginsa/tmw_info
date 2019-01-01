import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.lang.Runtime.getRuntime;

public class DAO {

    private static final Logger LOG = LogManager.getLogger();
    private static final String INSERT_INTERVAL =
            "INSERT INTO interval(username, start, end) VALUES (?, ?, ?)";
    private static final String INSERT_SNAPSHOT =
            "INSERT INTO snapshot(timestamp, names) VALUES (?, ?)";
    private static final String CREATE_SNAPSHOT_SCHEMA =
            "CREATE TABLE IF NOT EXISTS snapshot (" +
                    "id IDENTITY, " +
                    "timestamp TIMESTAMP, " +
                    "names VARCHAR(1024) NOT NULL)";

    private static final String CREATE_INTERVAL_SCHEMA =
            "CREATE TABLE IF NOT EXISTS interval (" +
                    "id IDENTITY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "start TIMESTAMP, " +
                    "end TIMESTAMP)";
    public static final String DB_USER_NAME = "admin";
    public static final String DB_USER_PASSWORD = "Yw3w79Ziw8DuhDZ";
    public static final String DB_URL = "jdbc:h2:~/tmw_informer;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=41444";

    private Connection connection;
    private Server webServer;
    private Server tcpServer;

    public void init() {
        establishConnection();
        startWebAndTcpServers();
        createSchemasIfNeeded();
        getRuntime().addShutdownHook(new Thread(() -> this.shutdown()));
    }

    private void establishConnection() {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException e) {
            LOG.error("Can't instantiate d2 database driver" + e);
        } catch (IllegalAccessException e) {
            LOG.error("Can't get access to d2 database driver" + e);
        } catch (ClassNotFoundException e) {
            LOG.error("Can't find d2 database driver" + e);
        }
        try {
            connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USER_NAME,
                    DB_USER_PASSWORD);
        } catch (SQLException e) {
            LOG.error("Can't get connection to d2 database driver" + e);
        }
        if (connection == null) {
            System.exit(1);
        }
    }

    private void startWebAndTcpServers() {
        try {
            webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082");
            webServer.start();
            tcpServer = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9092");
            tcpServer.start();
        } catch (SQLException e) {
            LOG.error("Can't instantiate d2 database driver" + e);
        }
    }

    private void shutdown() {
        LOG.info("start shutting down dao resources");
        if (webServer != null) {
            webServer.stop();
        }
        if (tcpServer != null) {
            tcpServer.stop();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Can't close connection to DB" + e);
            }
        }
        LOG.info("finished shutting down dao resources");
    }

    public void storeInterval(String user, LocalDateTime start, LocalDateTime end) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_INTERVAL);
            statement.setString(1, user);
            statement.setTimestamp(2, Timestamp.valueOf(start));
            statement.setTimestamp(3, Timestamp.valueOf(end));
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            LOG.error("Can't store interval  " + user + ' ' + start + ' ' + end);
        }
    }

    public void storeSnapshot(OnlineUsersSnapshot snapshot) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_SNAPSHOT);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(snapshot.getDateTime()));
            preparedStatement.setString(2, snapshot.getUsers().toString());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOG.error("Can't store snapshot " + snapshot);
        }
    }

    private void createSchemasIfNeeded() {
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            LOG.error("Can't create statement \n" + e);
            return;
        }
        try {
            statement.executeUpdate(CREATE_SNAPSHOT_SCHEMA);
        } catch (SQLException e) {
            LOG.error("Can't create snapshot schema \n" + e);
        }
        try {
            statement.executeUpdate(CREATE_INTERVAL_SCHEMA);
        } catch (SQLException e) {
            LOG.error("Can't create interval schema \n" + e);
        }
        try {
            statement.close();
        } catch (SQLException e) {
            LOG.error("Can't close statement");
        }
    }
}
