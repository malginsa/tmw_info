package org.tmw;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static java.lang.Runtime.getRuntime;

@Component
public class AppLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(AppLauncher.class);

    public static final int SCANNNING_INTERVAL = 9_000;

    @Autowired
    private OpenIntervalsKeeper openIntervalsKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        URL resource = AppLauncher.class.getClassLoader().getResource("src/main/webapp/WEB-INF/spring.xml");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        AppLauncher appLauncher = context.getBean(AppLauncher.class);
        appLauncher.startWebServer();
        appLauncher.launchIntervalUpdater();
    }

    private void startWebServer() {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        WebAppContext context = new WebAppContext("src/main/webapp", "/");

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
        OnlineUsersSupplier supplier = new OnlineUsersSupplier(); // TODO as bean
        openIntervalsKeeper.setStartSnaphot(supplier.getSnapshotNow());

        while (openIntervalsKeeper != null) {
            Thread.sleep(SCANNNING_INTERVAL); // TODO: refactor it using scheduler
            OnlineUsersSnapshot snapshot = supplier.getSnapshotNow();
            openIntervalsKeeper.processNextSnapshot(snapshot);
            LOG.info(snapshot.toString());
        }
    }
}
