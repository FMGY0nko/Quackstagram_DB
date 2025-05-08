import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.crypto.Data;

public class ImageUploadController extends TabViewController {

    private Database<Picture> picDatabase;
    private Database<SimplePicture> uploadedDatabase;

    ImageUploadController(Database<Picture> picDatabase, Database<SimplePicture> uploadedDatabase, Database<User> usersDB) {
        super(usersDB);
        this.picDatabase = picDatabase;
        this.uploadedDatabase = uploadedDatabase;
    }
    ImageUploadController()
    {
        super(new UsersDatabase());
        this.picDatabase = new PicturesDatabase();
        this.uploadedDatabase = new UploadedImagesDatabase();
    }

    private void saveImageInfo(String imageId, String username, String bio) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if(bio.isEmpty()) bio = "no caption";
        Picture p = new Picture(imageId, username, "", bio, timestamp, 0);
        picDatabase.save(p);
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
            String username = getLoggedInUserName(); // Read username from users.txt
            SimplePicture s = new SimplePicture(username, selectedFile.toPath().toString(), "");
            uploadedDatabase.save(s);
            // Save the bio and image ID to a text file
            saveImageInfo(s.getImageID(), s.getImagePosterName(), bioTextAreaText);
            ImageIcon preview = new ImageIcon(s.getImagePath());
            return preview;
        }
        return null;
    }
}

