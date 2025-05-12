import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageUploadController extends TabViewController {

    private void saveImageInfo(String originPath, String username, String bio) throws IOException {
        if(bio.isEmpty()) bio = "no caption";
        Picture p = new Picture("", username, originPath, bio, "", 0);
        saveIntoSQLDatabase(p);
    }

    public ImageIcon uploadAction(String bioTextAreaText) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("jpeg")) {
                JOptionPane.showMessageDialog(null, "Invalid file type, please select a PNG, JPG, or JPEG image.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            String username = getLoggedInUserName();
            saveImageInfo(selectedFile.getAbsolutePath(), username, bioTextAreaText);

            ImageIcon preview = new ImageIcon(selectedFile.getAbsolutePath());
            return preview;
        }
        return null;
    }

    private void saveIntoSQLDatabase(Picture p)
    {
        try{
            Connection c = DatabaseConnector.getConnection();
            int numberOfPosts = 0;
            String sql0 = "SELECT COALESCE(MAX(number),0) FROM uploaded_picture WHERE uploading_user = ?";
            PreparedStatement pstmt0 = c.prepareStatement(sql0);
            pstmt0.setString(1, p.getImagePosterName());
            ResultSet rs = pstmt0.executeQuery();
            if (rs.next()) {
                numberOfPosts = rs.getInt(1);
            }
            rs.close();
            pstmt0.close();
            String sql = "INSERT INTO uploaded_picture (uploading_user, number, bio) VALUES (?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, p.getImagePosterName());
            pstmt.setInt(2, numberOfPosts + 1);
            pstmt.setString(3, p.getCaption());
            pstmt.executeUpdate();
            pstmt.close();
            c.close();

            saveLocalImage(p.getImagePath(), p.getImagePosterName() + "_" + (numberOfPosts + 1));
        }catch (SQLException | IOException e) {
            e.printStackTrace();
    }

}

private void saveLocalImage(String originPath, String pictureID) throws IOException {
    String outputPath = "img/uploaded/" + pictureID + ".png";
    File inputFile = new File(originPath);
    File outputFile = new File(outputPath);

    if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();
    BufferedImage image = ImageIO.read(inputFile);
    if (image == null) {
        throw new IOException("unable to read image file: " + originPath);
    }
    ImageIO.write(image, "png", outputFile);
    System.out.println("image converted and saved to: " + outputFile.getAbsolutePath());
}
}


