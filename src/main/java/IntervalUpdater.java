import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class IntervalUpdater {

    private static final Logger LOG = LogManager.getLogger();
    public static final int SCANNNING_INTERVAL = 9_000;

    public static void main(String[] args) throws Exception {
        new IntervalUpdater().launch();
    }

    public void launch() throws IOException, InterruptedException {
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
