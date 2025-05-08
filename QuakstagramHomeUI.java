import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QuakstagramHomeUI extends TabViewWithHeader {
    private static final int IMAGE_WIDTH = WIDTH - 100;
    private static final int IMAGE_HEIGHT = 150;
    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95);
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    private HomeController controller;

    public QuakstagramHomeUI() {
        super("🐥 Quackstagram 🐥", "Quakstagram Home");
        controller = new HomeController();
        showContentPanel(); // must be the last line to ensure all fields are initialized first
    }

    private void populateContentPanel(JPanel panel, List<Picture> pictures) {

        for (Picture p : pictures) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(p.getImagePosterName());
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            String imageId = p.getImageID();

            ImageIcon imageIcon = loadCroppedImageIcon(p.getImagePath(), IMAGE_WIDTH, IMAGE_HEIGHT);
            if (imageIcon != null) {
                imageLabel.setIcon(imageIcon);
            } else {
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(p.getCaption());
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel likesLabel = new JLabel(p.getLikeLabel());
            likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton likeButton = createLikeButton(imageId, likesLabel, false);

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);

            panel.add(itemPanel);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Picture updatedPicture = controller.getPictureWithId(p.getImageID());
                    displayImage(updatedPicture);
                }
            });
            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5));
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    private void displayImage(Picture p) {
        imageViewPanel.removeAll();
        JLabel likesLabel = new JLabel(p.getLikeLabel());

        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon imageIcon = loadCroppedImageIcon(p.getImagePath(), WIDTH - 20, HEIGHT - 40);
        if (imageIcon != null) {
            fullSizeImageLabel.setIcon(imageIcon);
        } else {
            fullSizeImageLabel.setText("Image not found");
        }

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(p.getImagePosterName());
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);

        JButton likeButton = createLikeButton(p.getImageID(), likesLabel, true);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(p.getCaption())); // Description
        infoPanel.add(new JLabel(p.getLikeLabel())); // Likes
        infoPanel.add(likeButton);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();

        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private void refreshDisplayImage(String imageId) {
        Picture updatedPicture = controller.getPictureWithId(imageId);
        displayImage(updatedPicture);
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel p = new JPanel(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        List<Picture> sampleData = controller.createSampleData();
        populateContentPanel(contentPanel, sampleData);

        homePanel.add(scrollPane, BorderLayout.CENTER);

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        p.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home");
        return p;
    }

    private JButton createLikeButton(String imageId, JLabel likesLabel, boolean isDetailView) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR);
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false);
        likeButton.addActionListener(_ -> {
            controller.handleLikeAction(imageId, likesLabel);
            if (isDetailView) {
                refreshDisplayImage(imageId);
            } else {
                likesLabel.setText(controller.getPictureWithId(imageId).getLikeLabel());
            }
        });
        return likeButton;
    }
    

    private ImageIcon loadCroppedImageIcon(String imagePath, int maxWidth, int maxHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int width = Math.min(originalImage.getWidth(), maxWidth);
            int height = Math.min(originalImage.getHeight(), maxHeight);
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, width, height);
            return new ImageIcon(croppedImage);
        } catch (IOException ex) {
            return null;
        }
    }
}

