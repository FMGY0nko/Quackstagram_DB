import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class NotificationsController extends TabViewController {
    public String getElapsedTime(String timestamp) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter();
            
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

    private List<Notification> getAllNotificationsForReciever(String recieverUserName) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT * FROM picture_like WHERE uploading_user = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, recieverUserName);
            ResultSet rs = stmt.executeQuery();
            List<Notification> notifications = new ArrayList<>();
            while (rs.next()) {
                String transmitterName = rs.getString("liking_user");
                String timeStamp = rs.getTimestamp("time").toString();
                notifications.add(new Notification(recieverUserName, transmitterName, timeStamp));
            }
            rs.close();
            stmt.close();
            connection.close();
            return notifications;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllNotificationsMessagesForCurrentUser() {
        List<Notification> notifications = getAllNotificationsForReciever(getLoggedInUserName());
        List<String> notificationMessages = new ArrayList<>();
        for (Notification n : notifications) {
            String notificationMessage = n.getTransmitterName() + " liked your picture - "
                    + getElapsedTime(n.getTimeStamp())
                    + " ago";
            notificationMessages.add(notificationMessage);
        }
        return notificationMessages;
    }

}
