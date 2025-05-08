import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ImageUploadUI extends TabViewWithHeader {
    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;
    private JButton saveButton;
    private ImageUploadController controller;

    public ImageUploadUI() {
        super(" Upload Image 🐥", "Upload Image");
        controller = new ImageUploadController();
        showContentPanel();
    }

    void uploadAction(ActionEvent e)
    {
        ImageIcon preview = null;
        try {
            preview = controller.uploadAction(bioTextArea.getText());
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e1.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        if (preview == null) return;
        if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
            Image image = preview.getImage();
            int previewWidth = imagePreviewLabel.getWidth();
            int previewHeight = imagePreviewLabel.getHeight();
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            double widthRatio = (double) previewWidth / imageWidth;
            double heightRatio = (double) previewHeight / imageHeight;
            double scale = Math.min(widthRatio, heightRatio);
            int scaledWidth = (int) (scale * imageWidth);
            int scaledHeight = (int) (scale * imageHeight);

            preview.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
        }

        imagePreviewLabel.setIcon(preview);

        uploadButton.setText("Upload Another Image");

        JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
    } 
    

    private void saveBioAction(ActionEvent event) {
        String bioText = bioTextArea.getText();
        JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
    }
    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));


        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);

        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        saveButton = new JButton("Save Caption");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(this::saveBioAction);

        return contentPanel;
    }
}

