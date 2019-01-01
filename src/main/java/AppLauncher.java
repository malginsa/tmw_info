import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;

public class AppLauncher {

    private static final Logger LOG = LogManager.getLogger();

    public static final int SCANNNING_INTERVAL = 9_000;

    public static void main(String[] args) throws IOException, InterruptedException {
        AppLauncher appLauncher = new AppLauncher();
        appLauncher.startWebServer();
        appLauncher.launchIntervalUpdater();
    }

    private void startWebServer() {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        WebAppContext context = new WebAppContext("webapp", "/");

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            // fix for Windows, so Jetty doesn't lock files
            context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        }

        server.setHandler(context);
        try {
            server.start();
        } catch (Exception e) {
            LOG.error("Can't start Jetty server\n" + e);
        }

        getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (Exception e) {
                LOG.error("Can't stop Jetty server\n" + e);
            }
        }));
    }

    public void launchIntervalUpdater() throws IOException, InterruptedException {
        OnlineUsersSupplier supplier = new OnlineUsersSupplier();
        DAO dao = new DAO();
        dao.startDbServerAndEstablishConnection();
        dao.createSchemasIfNeeded();
        UserInfoStorage userInfoStorage = new UserInfoStorage(
                dao, supplier.getSnapshotNow());

        while (userInfoStorage != null) {
            Thread.sleep(SCANNNING_INTERVAL);
            OnlineUsersSnapshot snapshot = supplier.getSnapshotNow();
            userInfoStorage.processNextSnapshot(snapshot);
            dao.storeSnapshot(snapshot);
            LOG.info(snapshot);
        }
    }

}
