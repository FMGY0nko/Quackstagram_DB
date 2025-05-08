import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import java.util.List;

public class ExploreUI extends TabViewWithHeader {
    private static final int IMAGE_SIZE = WIDTH / 3;
    private ExploreController controller;

    public ExploreUI() {
        super("Explore 🐥", "Explore");
        controller = new ExploreController();
        showContentPanel();
    }

    private JPanel createMainContentPanel() {

        JTextField searchField = new JTextField("");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
    
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2));
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
        int delay = 300; 
        Timer timer = new Timer(delay, e -> updateImageGrid(searchField.getText(), imageGridPanel));
        timer.setRepeats(false);
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }
        });
    
        updateImageGrid("", imageGridPanel); // initially show all images
    
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane);
    
        return mainContentPanel;
    }

    private void displayImage(String imageID) {
        removeScreenContents();

        Picture p = controller.getPictureWithId(imageID);
        String username = p.getImagePosterName();
        String bio = p.getCaption();
        String timestampString = p.getTimeStamp();
        int likes = p.getLikesCount();

        String timeSincePosting = "Unknown";
        if (!timestampString.isEmpty()) {
            LocalDateTime timestamp = LocalDateTime.parse(timestampString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(timestamp, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton usernameLabel = new JButton(username);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(p.getImagePath()));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + likes);
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        addCustomComponentNorth(topPanel);
        addCustomComponentCenter(imageLabel);
        addCustomComponentSouth(bottomPanel);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");

        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);

        backButton.addActionListener(_ -> {
            removeScreenContents();
            redrawCommonComponents();
            showContentPanel();
        });
        final String finalUsername = username;

        usernameLabel.addActionListener(_ -> {
            User user = new User(finalUsername);
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            dispose();
        });

        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        addCustomComponentNorth(backButtonPanel);
        addCustomComponentCenter(containerPanel);

        revalidate();
        repaint();
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel p = createMainContentPanel();
        return p;
    }

    private void updateImageGrid(String username, JPanel imageGridPanel) {
        imageGridPanel.removeAll(); 
    
        List<SimplePicture> images = controller.getAllUploadedImages(username.trim());
    
        if (images.isEmpty()) {
            imageGridPanel.setLayout(new BorderLayout());
            User user = controller.doesUsernameExist(username.trim());
            if (user != null) {
                JButton goToUserProfileButton = new JButton("View " + user.getUsername() + "'s profile");
                goToUserProfileButton.setPreferredSize(new Dimension(200, 50));
                goToUserProfileButton.addActionListener(e -> {
                    InstagramProfileUI profileUI = new InstagramProfileUI(user);
                    profileUI.setVisible(true);
                    dispose();
                });
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(goToUserProfileButton);
                imageGridPanel.add(buttonPanel, BorderLayout.CENTER);
            } else {
                imageGridPanel.add(new JLabel("No results found."), BorderLayout.CENTER);
            }
        } else {
            imageGridPanel.setLayout(new GridLayout(0, 3, 2, 2));
            for (SimplePicture image : images) {
                ImageIcon icon = new ImageIcon(image.getImagePath());
                Image scaled = icon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaled));
    
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayImage(image.getImageID());
                    }
                });
                imageGridPanel.add(imageLabel);
            }
        }
        imageGridPanel.revalidate();
        imageGridPanel.repaint();
    }
    
}
