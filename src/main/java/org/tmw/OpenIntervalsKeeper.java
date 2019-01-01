package org.tmw;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenIntervalsKeeper {

    private final DAO dao;
    private final Map<String, LocalDateTime> startDateTime = new HashMap<>();
    private OnlineUsersSnapshot previousSnapshot;

    public OpenIntervalsKeeper(DAO dao,
                               OnlineUsersSnapshot snapshot) {
        this.dao = dao;
        LocalDateTime dateTime = snapshot.getDateTime();
        previousSnapshot = snapshot;
        for (String user : snapshot.getUsers()) {
            startDateTime.put(user, dateTime);
        }
    }

    public void processNextSnapshot(OnlineUsersSnapshot newSnapshot) {
        this.addNewUsers(newSnapshot);
        List<String> logOffUsers = this.removeLoggedOffUsers(newSnapshot);
        this.storeIntervals(logOffUsers);
        for (String logOffUser : logOffUsers) {
            startDateTime.remove(logOffUser);
        }
        previousSnapshot = newSnapshot;
    }

    private void storeIntervals(List<String> users) {
        for (String user : users) {
            dao.storeInterval(
                    user,
                    startDateTime.get(user),
                    previousSnapshot.getDateTime());
        }
    }

    private List<String> removeLoggedOffUsers(OnlineUsersSnapshot snapshot) {
        List<String> loggedOffUsers = new ArrayList<>(previousSnapshot.getUsers());
        loggedOffUsers.removeAll(snapshot.getUsers());
        return loggedOffUsers;
    }

    private void addNewUsers(OnlineUsersSnapshot newSnapshot) {
        List<String> newUsers = new ArrayList<>(newSnapshot.getUsers());
        newUsers.removeAll(previousSnapshot.getUsers());
        for (String newUser : newUsers) {
            startDateTime.put(newUser, newSnapshot.getDateTime());
        }
    }
}
