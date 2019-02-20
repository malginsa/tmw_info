package org.tmw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenIntervalsKeeper {

    @Autowired
    private DAO dao;

    private final Map<String, LocalDateTime> startDateTime = new HashMap<>();
    private OnlineUsersSnapshot previousSnapshot;

    public OpenIntervalsKeeper() {
        System.out.println("OpenInervalsKeeper " + this.toString() + " is created");
    }

    public void setStartSnaphot(OnlineUsersSnapshot snapshot) {
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
        dao.storeSnapshot(newSnapshot);
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
