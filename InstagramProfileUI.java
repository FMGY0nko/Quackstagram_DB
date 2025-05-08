import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.List;

public class InstagramProfileUI extends TabView {

    private static final int PROFILE_IMAGE_SIZE = 80;
    private static final int GRID_IMAGE_SIZE = WIDTH / 3;
    private JPanel imagePanel;
    private JPanel upperPanel;
    private ProfileController controller;
    private JLabel followersCountLabel;

    public InstagramProfileUI(User user) {
        super("DACS profile");
        controller = new ProfileController(user);
        showContentPanel();
    }

    public InstagramProfileUI() {
        super("DACS profile");
        showContentPanel();
    }

    private JPanel createHeaderPanel() {
        boolean isCurrentUser = controller.isCurrentUserLoggedInUser();
        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        ImageIcon pp = controller.getCurrentlyViewedUserProfilePicture();
        ImageIcon profilePicture = new ImageIcon(pp.getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profilePicture);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user" + controller.getCurrentlyViewedUser().getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(controller.getCurrentlyViewedUser().getPostsCount()), "Posts"));
        followersCountLabel = createStatLabel(Integer.toString(controller.getCurrentlyViewedUser().getFollowersCount()),
                "Followers");
        statsPanel.add(followersCountLabel);
        statsPanel.add(createStatLabel(Integer.toString(controller.getCurrentlyViewedUser().getFollowingCount()),
                "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        JButton followButton;
        if (isCurrentUser) {
            followButton = new JButton("Edit Profile");
        } else {
            followButton = new JButton("Follow");
            
            if (controller.isCurrentUserFollowed()) {
                followButton.setText("Following");
            }
            followButton.addActionListener(_ -> {
                controller.handleFollowAction();
                followersCountLabel.setText(createStatLabelText(
                        (Integer.toString(controller.getCurrentlyViewedUser().getFollowersCount())), "Followers"));
                followButton.setText("Following");
            });
        }

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
        followButton.setBackground(new Color(225, 228, 232));
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(controller.getCurrentlyViewedUser().getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JTextArea profileBio = new JTextArea(controller.getCurrentlyViewedUser().getBio());
        System.out.println("This is the bio " + controller.getCurrentlyViewedUser().getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);

        return headerPanel;

    }

    private void initializeImageGrid() {
        imagePanel.removeAll();
        imagePanel.setLayout(new GridLayout(0, 3, 5, 5));

        List<ImageIcon> images = controller.getProfileImages(controller.getCurrentlyViewedUser());

        for (ImageIcon image : images) {
            ImageIcon imageIcon = new ImageIcon(image.getImage()
                    .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(imageIcon); 
                }
            });
            imagePanel.add(imageLabel);

        }
        imagePanel.revalidate();
        imagePanel.repaint();
        revalidate();
        repaint();
    }

    private void displayImage(ImageIcon imageIcon) {
        imagePanel.removeAll(); // Remove existing content
        imagePanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> {
            initializeImageGrid();
            revalidate();
            repaint();
        });
        imagePanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel(createStatLabelText(number, text),
                SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private String createStatLabelText(String number, String text) {
        return "<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>";
    }

    @Override
    protected JPanel createContentPanel() {
        imagePanel = new JPanel();
        upperPanel = createHeaderPanel();

        initializeImageGrid();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

}