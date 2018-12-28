import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Runtime.*;

public class IntervalUpdater {

    private static final Logger LOG = LogManager.getLogger();
    public static final int SCANNNING_INTERVAL = 9_000;

    public static void main(String[] args) throws Exception {

        OnlineUsersSupplier onlineUsersSupplier = new OnlineUsersSupplier();
        DAO dao = new DAO();
        getRuntime().addShutdownHook(new Thread(dao::closeConnection));
        dao.establishDbConnection();
        dao.startDbServer();

        dao.createSchemasIfNeeded();
        UserInfoStorage userInfoStorage = new UserInfoStorage(
                dao, onlineUsersSupplier.getSnapshotNow());

        while (userInfoStorage != null) {
            Thread.sleep(SCANNNING_INTERVAL);
            OnlineUsersSnapshot snapshot =
                    onlineUsersSupplier.getSnapshotNow();
            userInfoStorage.processNextSnapshot(snapshot);
            dao.storeSnapshot(snapshot);
            LOG.info(snapshot);
        }
    }
}
