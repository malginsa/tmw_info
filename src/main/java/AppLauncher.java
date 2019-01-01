import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;

public class AppLauncher {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) throws IOException, InterruptedException {
        startJetty();
//        new IntervalUpdater().launch();
    }

    private static void startJetty() {
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
}
