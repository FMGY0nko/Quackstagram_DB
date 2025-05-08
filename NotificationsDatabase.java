import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDatabase extends Database<Notification> {

    @Override
    protected List<Notification> retrieveAll() {
        List<Notification> temp = new ArrayList<Notification>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "notifications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String reciever = parts[0].trim();
                String transmitter = parts[1].trim();
                String timestamp = parts[3].trim();
                Notification n = new Notification(reciever, transmitter, timestamp);
                temp.add(n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp.reversed();
    }

    @Override
    public boolean save(Notification m) {
        String notification = String.format("%s; %s; %s; %s\n", m.getRecieverName(), m.getTransmitterName(), m.getImageID(), m.getTimeStamp());
        try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            notificationWriter.write(notification);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void loadPath() {
    }
}
