import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationsUI extends TabViewWithHeader {
    private NotificationsController controller;

    public NotificationsUI() {
        super(" Notifications 🐥", "Notifications");
        controller = new NotificationsController();
        showContentPanel(); //must be the last line to ensure all fields are initialized first
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        List<String> notificationsForCurrentUser = controller.getAllNotificationsMessagesForCurrentUser();

        for (String n : notificationsForCurrentUser) {
            JPanel notificationPanel = new JPanel(new BorderLayout());
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JLabel notificationLabel = new JLabel(n);
            notificationPanel.add(notificationLabel, BorderLayout.CENTER);
            contentPanel.add(notificationPanel);
        }

        JPanel p = new JPanel(new BorderLayout());
        p.add(scrollPane, BorderLayout.CENTER);
        return p;
    }
}
