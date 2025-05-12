import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationsController extends TabViewController{
    private Database<Notification> notificationDB;
    NotificationsController(Database<Notification> notificationDB, UsersDatabase usersDB) {
        this.notificationDB = notificationDB;
    }
    NotificationsController()
    {
        this.notificationDB = new NotificationsDatabase();
    }
        public String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }
        return timeElapsed.toString();
    }

    private List<Notification> getAllNotificationsForReciever(String recieverUserName)
    {
        return notificationDB.getMatching((t) -> recieverNameMatchPredicate(t, recieverUserName));
    }

    private boolean recieverNameMatchPredicate(Notification t, String recieverUserName)
    {
        return t.getRecieverName().equals(recieverUserName);
    }
    public List<String> getAllNotificationsMessagesForCurrentUser() {
        List<Notification> notifications = getAllNotificationsForReciever(getLoggedInUserName());
        List<String> notificationMessages = new ArrayList<>();
        for(Notification n : notifications)
        {
            String notificationMessage = n.getTransmitterName() + " liked your picture - "
            + getElapsedTime(n.getTimeStamp())
            + " ago";
            notificationMessages.add(notificationMessage);
        }
        return notificationMessages;
    }
    
}

